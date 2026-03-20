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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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

@Suppress("TooGenericExceptionCaught", "RethrowCaughtException", "LoopWithTooManyJumpStatements")
class ChatWebSocketDataSourceImpl(
    private val httpClient: HttpClient,
) : ChatWebSocketDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    override fun observeMessages(conversationId: String): Flow<ChatMessageModel> = channelFlow {
        val origin = BuildKonfig.BASE_URL.trimEnd('/')
        var attempt = 0

        while (true) {
            attempt++
            Napier.w(tag = TAG, message = "╠══ attempt=$attempt conversationId=$conversationId")

            val ctx = try {
                obtainWsConnectContext()
            } catch (e: CancellationException) {
                throw e
            }

            if (ctx == null) {
                delay(RECONNECT_DELAY_MS)
                continue
            }

            Napier.w(tag = TAG, message = "╠══ connecting: channel=${ctx.channel}")

            val sessionError = runCatching {
                httpClient.webSocket(
                    urlString = BuildKonfig.WS_BASE_URL,
                    request = {
                        header("Origin", origin)
                        header("Connection", "Upgrade")
                    },
                ) {
                    Napier.w(tag = TAG, message = "╠══ WS SESSION OPENED")
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
                    Napier.w(tag = TAG, message = "╚══ WS cancelled — stopping")
                    throw sessionError
                }

                null -> Unit
                else -> Napier.e(tag = TAG, message = "╠══ session error: $sessionError")
            }

            Napier.w(
                tag = TAG,
                message = "╠══ reconnecting in ${RECONNECT_DELAY_MS}ms (attempt=$attempt)…"
            )
            delay(RECONNECT_DELAY_MS)
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
                message = "╠══ no connection token (obtain__token) — retry",
            )
            return null
        }

        val channel = extractChannelFromJwt(tokenData.subscriptionToken)
        if (channel == null) {
            Napier.e(
                tag = TAG,
                message = "╠══ cannot extract channel from subscription JWT — retry"
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

        ws.sendLine(
            json.encodeToString(
                CentrifugoConnectCommand.serializer(),
                CentrifugoConnectCommand(
                    id = nextId(),
                    connect = CentrifugoConnectParams(token = ctx.connectToken),
                ),
            ),
        )
        Napier.w(tag = TAG, message = "╠══ → CONNECT sent")

        for (frame in ws.incoming) {
            when (frame) {
                is Frame.Text -> handleTextFrame(
                    text = frame.readText(),
                    ws = ws,
                    producer = producer,
                    conversationId = conversationId,
                    ctx = ctx,
                    nextId = nextId,
                )

                is Frame.Close -> {
                    Napier.w(tag = TAG, message = "╠══ ← CLOSE frame — session ends")
                    return
                }

                else -> Unit
            }
        }

        val closeReason = runCatching { ws.closeReason.await() }.getOrNull()
        Napier.w(
            tag = TAG,
            message = "╠══ CLOSE: code=${closeReason?.code} msg='${closeReason?.message}'"
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
    ) {
        Napier.w(tag = TAG, message = "╠══ ← RAW: $text")

        for (line in text.lineSequence()) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            if (trimmed == "{}") {
                ws.sendLine("{}")
                Napier.w(tag = TAG, message = "╠══ ↔ PING/PONG")
                continue
            }

            val msg =
                runCatching { json.decodeFromString<CentrifugoServerMessage>(line) }.getOrElse { e ->
                    Napier.e(tag = TAG, message = "╠══ PARSE ERROR: $e  raw=$line")
                    continue
                }

            when {
                msg.connectResult != null -> {
                    Napier.w(
                        tag = TAG,
                        message = "╠══ CONNECT OK: client=${msg.connectResult.client}"
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
                    Napier.w(tag = TAG, message = "╠══ → SUBSCRIBE channel=${ctx.channel}")
                }

                msg.subscribeResult != null ->
                    Napier.w(
                        tag = TAG,
                        message = "╠══ SUBSCRIBE OK: recoverable=${msg.subscribeResult.recoverable} ← READY",
                    )

                msg.push != null -> handlePush(msg.push, conversationId, producer)

                msg.error != null ->
                    Napier.e(
                        tag = TAG,
                        message = "╠══ SERVER ERROR: code=${msg.error.code} msg=${msg.error.message}"
                    )

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
            Napier.w(tag = TAG, message = "╠══ PUSH: pub.data is null — skip")
            return
        }

        val messageElement = (rawData as? JsonObject)?.get("data") ?: rawData

        val model = runCatching {
            json.decodeFromJsonElement(MessageDto.serializer(), messageElement)
        }.getOrElse { e ->
            Napier.e(
                tag = TAG,
                message = "╠══ PUSH: cannot parse MessageRemoteDto: $e  raw=$rawData"
            )
            return
        }

        Napier.w(
            tag = TAG,
            message = "╠══ PUSH: id=${model.id} convId=${model.conversationId} kind=${model.kind} text=${
                model.text
            }",
        )

        if (model.conversationId.toString() != conversationId) {
            Napier.w(
                tag = TAG,
                message = "╠══ PUSH filtered: msgConvId=${model.conversationId} != screenConvId=$conversationId",
            )
            return
        }

        Napier.w(tag = TAG, message = "╠══ PUSH ✓ EMITTING id=${model.id}")
        producer.send(model.toModel())
    }

    private suspend fun fetchSubscriptionTokens(): SubscriptionTokenData {
        val url = BuildKonfig.BASE_URL + "/api/conversations/obtain_subscription_token"
        Napier.w(tag = TAG, message = "╠══ GET $url")
        val response = httpClient.get(url)
        val statusCode = response.status.value
        val body = response.bodyAsText()
        Napier.w(tag = TAG, message = "╠══ tokens HTTP $statusCode body=${body}")

        if (statusCode != HTTP_OK) error("obtain_subscription_token returned HTTP $statusCode")

        return json.decodeFromString<SubscriptionTokenResponse>(body).data
    }

    private suspend fun fetchWsAuthToken(): String? = runCatching {
        val url = BuildKonfig.BASE_URL + "/api/auth/obtain_ws_auth_token"

        Napier.w(tag = TAG, message = "╠══ GET $url")

        val response = httpClient.get(url)
        val statusCode = response.status.value
        val body = response.bodyAsText()
        val dataKeys =
            (json.parseToJsonElement(body).jsonObject["data"] as? JsonObject)?.keys?.joinToString()

        Napier.w(
            tag = TAG,
            message = "╠══ ws_auth_token HTTP $statusCode bodyLen=${body.length} keys=$dataKeys"
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
        val decoded = Base64.UrlSafe.decode(payloadB64).decodeToString()
        Napier.w(tag = TAG, message = "╠══ JWT payload: $decoded")
        json.parseToJsonElement(decoded).jsonObject["channel"]?.jsonPrimitive?.content
    }.getOrElse { e ->
        Napier.e(tag = TAG, message = "╠══ JWT decode FAILED", throwable = e)
        null
    }

    private companion object {
        private const val TAG = "ChatWebSocket"
        private const val RECONNECT_DELAY_MS = 3_000L
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
    }

}
