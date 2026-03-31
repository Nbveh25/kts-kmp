package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatWebSocketDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.MessageDto
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.toModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.min
import kotlin.random.Random

@Suppress("TooGenericExceptionCaught", "RethrowCaughtException", "LoopWithTooManyJumpStatements", "ReturnCount")
class ChatWebSocketDataSourceImpl(
    private val httpClient: HttpClient,
    private val json: Json,
) : ChatWebSocketDataSource {

    override fun observeMessages(conversationId: String): Flow<ChatMessageModel> = channelFlow {
        val origin = BuildKonfig.BASE_URL.trimEnd('/')
        val backoff = ReconnectBackoff(
            minDelayMs = RECONNECT_DELAY_MS,
            maxDelayMs = MAX_RECONNECT_DELAY_MS,
        )

        while (coroutineContext.isActive) {
            val attempt = backoff.attempt + 1
            Napier.w(tag = TAG, message = "ws: attempt=$attempt convId=$conversationId")

            val ctx = try {
                obtainWsConnectContext()
            } catch (e: CancellationException) {
                throw e
            }

            if (ctx == null) {
                delay(backoff.nextDelayMs())
                continue
            }

            Napier.w(tag = TAG, message = "ws: connecting (channel=${ctx.channel})")

            val sessionError = runCatching {
                httpClient.webSocket(
                    urlString = BuildKonfig.WS_BASE_URL,
                    request = {
                        header("Origin", origin)
                    },
                ) {
                    Napier.w(tag = TAG, message = "ws: session opened")
                    runSession(
                        ws = this,
                        producer = this@channelFlow,
                        conversationId = conversationId,
                        ctx = ctx,
                    )
                }
            }.exceptionOrNull()

            when (sessionError) {
                is CancellationException -> {
                    Napier.w(tag = TAG, message = "ws: cancelled — stop observing")
                    throw sessionError
                }

                null -> {
                    backoff.reset()
                }

                else -> {
                    Napier.e(tag = TAG, message = "ws: session error: $sessionError")
                }
            }

            val delayMs = backoff.nextDelayMs()
            Napier.w(tag = TAG, message = "ws: reconnect in ${delayMs}ms (attempt=$attempt)…")
            delay(delayMs)
            yield()
        }
    }

    private data class WsConnectContext(
        val channel: String,
        val subscriptionToken: String,
        val connectToken: String,
    )

    /** Токены на каждый заход в цикл — JWT с ttl, при реконнекте нужны свежие. */
    private suspend fun obtainWsConnectContext(): WsConnectContext? {
        val tokenData = try {
            fetchSubscriptionTokens()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Napier.e(
                tag = TAG,
                message = "╠══ fetchTokens FAILED${e.networkHint()}",
                throwable = e,
            )
            return null
        }

        val connectToken = fetchWsAuthToken() ?: tokenData.connectionToken
        if (connectToken == null) {
            Napier.e(
                tag = TAG,
                message = "ws: no connection token — retry",
            )
            return null
        }

        val channel = extractChannelFromJwt(tokenData.subscriptionToken)
        if (channel == null) {
            Napier.e(
                tag = TAG,
                message = "ws: cannot extract channel from subscription JWT — retry"
            )
            return null
        }

        return WsConnectContext(
            channel = channel,
            subscriptionToken = tokenData.subscriptionToken,
            connectToken = connectToken,
        )
    }

    private suspend fun runSession(
        ws: DefaultClientWebSocketSession,
        producer: ProducerScope<ChatMessageModel>,
        conversationId: String,
        ctx: WsConnectContext,
    ) {
        var commandId = 1
        val nextId = { commandId++ }
        var subscribed = false

        ws.sendLine(
            json.encodeToString(
                CentrifugoConnectCommand.serializer(),
                CentrifugoConnectCommand(
                    id = nextId(),
                    connect = CentrifugoConnectParams(token = ctx.connectToken),
                ),
            ),
        )
        Napier.w(tag = TAG, message = "ws: → CONNECT sent")

        for (frame in ws.incoming) {
            if (!currentCoroutineContext().isActive) return
            when (frame) {
                is Frame.Text -> handleTextFrame(
                    text = frame.readText(),
                    ws = ws,
                    producer = producer,
                    conversationId = conversationId,
                    ctx = ctx,
                    nextId = nextId,
                    onSubscribed = { subscribed = true },
                )

                is Frame.Close -> {
                    Napier.w(tag = TAG, message = "ws: ← CLOSE frame — session ends")
                    return
                }

                else -> Unit
            }
        }

        val closeReason = runCatching { ws.closeReason.await() }.getOrNull()
        Napier.w(
            tag = TAG,
            message = "ws: closed: code=${closeReason?.code} msg='${closeReason?.message}' subscribed=$subscribed"
        )
    }

    private suspend fun DefaultClientWebSocketSession.sendLine(payload: String) {
        val text = if (payload.endsWith('\n')) payload else "$payload\n"
        send(Frame.Text(text))
    }

    private suspend fun handleTextFrame(
        text: String,
        ws: DefaultClientWebSocketSession,
        producer: ProducerScope<ChatMessageModel>,
        conversationId: String,
        ctx: WsConnectContext,
        nextId: () -> Int,
        onSubscribed: () -> Unit,
    ) {
        Napier.d(tag = TAG, message = "ws: ← text len=${text.length}")

        for (line in text.lineSequence()) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            if (trimmed == "{}") { // ping
                ws.sendLine("{}")
                Napier.d(tag = TAG, message = "ws: ↔ PING/PONG")
                continue
            }

            val msg =
                runCatching { json.decodeFromString<CentrifugoServerMessage>(trimmed) }.getOrElse { e ->
                    Napier.e(tag = TAG, message = "ws: parse error: $e")
                    continue
                }

            when {
                msg.connectResult != null -> {
                    Napier.w(
                        tag = TAG,
                        message = "ws: CONNECT OK (client=${msg.connectResult.client})"
                    )
                    ws.sendLine(
                        json.encodeToString(
                            CentrifugoSubscribeCommand.serializer(),
                            CentrifugoSubscribeCommand(
                                id = nextId(),
                                subscribe = CentrifugoSubscribeParams(
                                    channel = ctx.channel,
                                    token = ctx.subscriptionToken,
                                ),
                            ),
                        ),
                    )
                    Napier.w(tag = TAG, message = "ws: → SUBSCRIBE channel=${ctx.channel}")
                }

                msg.subscribeResult != null -> {
                    Napier.w(
                        tag = TAG,
                        message = "ws: SUBSCRIBE OK (recoverable=${msg.subscribeResult.recoverable}) ← READY",
                    )
                    onSubscribed()
                }

                msg.push != null -> handlePush(msg.push, conversationId, producer)

                msg.error != null -> {
                    Napier.e(
                        tag = TAG,
                        message = "ws: SERVER ERROR: code=${msg.error.code} msg=${msg.error.message}"
                    )
                    error("ws: server error ${msg.error.code}: ${msg.error.message}")
                }

                else -> Napier.w(tag = TAG, message = "╠══ UNKNOWN frame (id=${msg.id}): $line")
            }
        }
    }

    private suspend fun handlePush(
        push: CentrifugoPush,
        conversationId: String,
        producer: ProducerScope<ChatMessageModel>,
    ) {
        val rawData = push.pub?.data ?: run {
            Napier.d(tag = TAG, message = "ws: PUSH: pub.data is null — skip")
            return
        }

        val messageElement = (rawData as? JsonObject)?.get("data") ?: rawData

        val model = runCatching {
            json.decodeFromJsonElement(MessageDto.serializer(), messageElement)
        }.getOrElse { e ->
            Napier.e(
                tag = TAG,
                message = "ws: PUSH: cannot parse MessageDto: $e"
            )
            return
        }

        Napier.d(tag = TAG, message = "ws: PUSH: id=${model.id} convId=${model.conversationId} kind=${model.kind}")

        if (model.conversationId.toString() != conversationId) {
            Napier.d(tag = TAG, message = "ws: PUSH filtered: ${model.conversationId} != $conversationId")
            return
        }

        Napier.d(tag = TAG, message = "ws: PUSH ✓ emitting id=${model.id}")
        producer.send(model.toModel())
    }

    private suspend fun fetchSubscriptionTokens(): SubscriptionTokenData {
        val url = BuildKonfig.BASE_URL + "/api/conversations/obtain_subscription_token"
        Napier.d(tag = TAG, message = "ws: GET obtain_subscription_token")
        val response = httpClient.get(url)
        val statusCode = response.status.value
        val body = response.bodyAsText()
        Napier.d(tag = TAG, message = "ws: tokens HTTP $statusCode bodyLen=${body.length}")

        if (statusCode != HTTP_OK) error("obtain_subscription_token returned HTTP $statusCode")

        return json.decodeFromString<SubscriptionTokenResponse>(body).data
    }

    private suspend fun fetchWsAuthToken(): String? = runCatching {
        val url = BuildKonfig.BASE_URL + "/api/auth/obtain_ws_auth_token"

        Napier.d(tag = TAG, message = "ws: GET obtain_ws_auth_token")

        val response = httpClient.get(url)
        val statusCode = response.status.value
        val body = response.bodyAsText()
        val dataKeys =
            (json.parseToJsonElement(body).jsonObject["data"] as? JsonObject)?.keys?.joinToString()

        Napier.w(
            tag = TAG,
            message = "ws: ws_auth_token HTTP $statusCode bodyLen=${body.length} keys=$dataKeys"
        )

        if (statusCode != HTTP_OK) error("obtain_ws_auth_token returned HTTP $statusCode")

        extractConnectJwt(json.parseToJsonElement(body).jsonObject)
    }.getOrElse { e ->
        Napier.e(tag = TAG, message = "╠══ fetchWsAuthToken FAILED", throwable = e)
        null
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun extractChannelFromJwt(token: String): String? = runCatching {
        val payloadB64 = token.split(".").getOrNull(1) ?: return@runCatching null
        val decoded = Base64.UrlSafe.decode(payloadB64.base64UrlPadded()).decodeToString()
        Napier.d(tag = TAG, message = "ws: JWT payload len=${decoded.length}")
        json.parseToJsonElement(decoded).jsonObject["channel"]?.jsonPrimitive?.content
    }.getOrElse { e ->
        Napier.e(tag = TAG, message = "╠══ JWT decode FAILED", throwable = e)
        null
    }

    private companion object {
        private const val TAG = "ChatWebSocket"
        private const val RECONNECT_DELAY_MS = 3_000L
        private const val MAX_RECONNECT_DELAY_MS = 30_000L
        private const val HTTP_OK = 200

        private fun Throwable.networkHint(): String = when {
            this::class.simpleName == "UnknownHostException" ->
                " (проверьте интернет и CABINET_DOMAIN в local.properties)"

            message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                " (проверьте интернет и CABINET_DOMAIN в local.properties)"

            this::class.simpleName == "ConnectException" -> " (сервер недоступен, проверьте сеть)"
            message?.contains("Connection refused", ignoreCase = true) == true ->
                " (сервер недоступен, проверьте сеть)"

            else -> ""
        }

        private fun jwtOrNull(raw: String?): String? = raw?.takeIf { it.startsWith("eyJ") }

        /** Разные формы ответа: token в корне, data — строка или объект с token / connection_token / … */
        private fun extractConnectJwt(root: JsonObject): String? {
            jwtOrNull(root["token"]?.jsonPrimitive?.content)?.let { return it }
            when (val data = root["data"]) {
                is JsonPrimitive -> jwtOrNull(data.content)?.let { return it }
                is JsonObject -> {
                    for (key in listOf("token", "connection_token", "auth_token", "access_token")) {
                        jwtOrNull(data[key]?.jsonPrimitive?.content)?.let { return it }
                    }
                    for (v in data.values) {
                        jwtOrNull((v as? JsonPrimitive)?.content)?.let { return it }
                    }
                }

                else -> Unit
            }
            return null
        }

        private fun String.base64UrlPadded(): String {
            val mod = length % 4
            if (mod == 0) return this
            return this + "=".repeat(4 - mod)
        }
    }

}

private class ReconnectBackoff(
    private val minDelayMs: Long,
    private val maxDelayMs: Long,
) {
    var attempt: Int = 0
        private set

    fun reset() {
        attempt = 0
    }

    fun nextDelayMs(): Long {
        attempt++
        val exp = min(10, attempt) // guard from overflow
        val base = minDelayMs * (1L shl exp)
        val capped = min(maxDelayMs, base)
        val jitter = (capped * 0.2).toLong().coerceAtLeast(1L)
        return (capped - jitter) + Random.nextLong(0, jitter + 1)
    }
}
