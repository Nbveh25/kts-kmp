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
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.MessageRemoteDto
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.toModel
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ChatWebSocketDataSourceImpl(
    private val httpClient: HttpClient,
    private val spaceProvider: SpaceProvider,
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

            // Токены запрашиваем при каждом attempt — JWT имеет ttl, при реконнекте нужны свежие
            val tokenData: SubscriptionTokenData? = try {
                fetchSubscriptionTokens()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val hint = when {
                    e::class.simpleName == "UnknownHostException" ||
                            (e.message?.contains(
                                "Unable to resolve host",
                                ignoreCase = true
                            ) == true) ->
                        " (проверьте интернет и CABINET_DOMAIN в local.properties)"

                    e::class.simpleName == "ConnectException" ||
                            (e.message?.contains(
                                "Connection refused",
                                ignoreCase = true
                            ) == true) ->
                        " (сервер недоступен, проверьте сеть)"

                    else -> ""
                }
                Napier.e(tag = TAG, message = "╠══ fetchTokens FAILED$hint", throwable = e)
                null
            }

            if (tokenData == null) {
                delay(RECONNECT_DELAY_MS)
                continue
            }

            val subscriptionToken = tokenData.subscriptionToken
            // connectToken — только connection JWT (без channel). subscriptionToken — только для subscribe.
            val connectToken = fetchWsAuthToken()
                ?: tokenData.connectionToken

            if (connectToken == null) {
                Napier.e(
                    tag = TAG,
                    message = "╠══ no connection token (obtain_ws_auth_token and obtain_subscription_token.token both empty) — skip, retry"
                )
                delay(RECONNECT_DELAY_MS)
                continue
            }

            val channel = extractChannelFromJwt(subscriptionToken)
            if (channel == null) {
                Napier.e(
                    tag = TAG,
                    message = "╠══ cannot extract channel from subscription JWT, will retry"
                )
                delay(RECONNECT_DELAY_MS)
                continue
            }

            Napier.w(tag = TAG, message = "╠══ connecting: channel=$channel")

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
                        channel = channel,
                        subscriptionToken = subscriptionToken,
                        connectToken = connectToken,
                    )
                }
            }.exceptionOrNull()

            if (sessionError is CancellationException) {
                Napier.w(tag = TAG, message = "╚══ WS cancelled — stopping")
                throw sessionError
            }
            if (sessionError != null) {
                Napier.e(tag = TAG, message = "╠══ session error: $sessionError")
            }

            Napier.w(
                tag = TAG,
                message = "╠══ reconnecting in ${RECONNECT_DELAY_MS}ms (attempt=$attempt)…"
            )
            delay(RECONNECT_DELAY_MS)
        }
    }

    // ─── Centrifugo session ──────────────────────────────────────────────────────

    private suspend fun runSession(
        ws: DefaultClientWebSocketSession,
        producer: ProducerScope<ChatMessageModel>,
        conversationId: String,
        channel: String,
        subscriptionToken: String,
        connectToken: String,
    ) {
        var commandId = 1

        // 1. Connect — сервер отклоняет пустой connect (3501), используем subscription_token
        val connectCmd = json.encodeToString(
            CentrifugoConnectCommand.serializer(),
            CentrifugoConnectCommand(
                id = commandId++,
                connect = CentrifugoConnectParams(token = connectToken),
            ),
        ) + "\n"

        Napier.w(tag = TAG, message = "╠══ → CONNECT cmd=$connectCmd")
        ws.send(Frame.Text(connectCmd))

        for (frame in ws.incoming) {
            when (frame) {
                is Frame.Text -> handleTextFrame(
                    text = frame.readText(),
                    ws = ws,
                    producer = producer,
                    conversationId = conversationId,
                    channel = channel,
                    subscriptionToken = subscriptionToken,
                    nextId = { commandId++ },
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

    private suspend fun handleTextFrame(
        text: String,
        ws: DefaultClientWebSocketSession,
        producer: ProducerScope<ChatMessageModel>,
        conversationId: String,
        channel: String,
        subscriptionToken: String,
        nextId: () -> Int,
    ) {
        Napier.w(tag = TAG, message = "╠══ ← RAW: $text")

        // Centrifugo шлёт несколько JSON-объектов в одном фрейме, разделённых \n
        text.split("\n").forEach { line ->
            if (line.isBlank()) return@forEach

            // Centrifugo ping — отвечаем пустым объектом
            if (line.trim() == "{}") {
                ws.send(Frame.Text("{}\n"))
                Napier.w(tag = TAG, message = "╠══ ↔ PING/PONG")
                return@forEach
            }

            val msg = runCatching {
                json.decodeFromString<CentrifugoServerMessage>(line)
            }.getOrElse { e ->
                Napier.e(tag = TAG, message = "╠══ PARSE ERROR: $e  raw=$line")
                return@forEach
            }

            when {
                msg.connectResult != null -> {
                    Napier.w(
                        tag = TAG,
                        message = "╠══ CONNECT OK: client=${msg.connectResult.client}"
                    )

                    // 2. Subscribe на канал проекта
                    val subscribeCmd = json.encodeToString(
                        CentrifugoSubscribeCommand.serializer(),
                        CentrifugoSubscribeCommand(
                            id = nextId(),
                            subscribe = CentrifugoSubscribeParams(
                                channel = channel,
                                token = subscriptionToken,
                            ),
                        ),
                    ) + "\n"

                    Napier.w(tag = TAG, message = "╠══ → SUBSCRIBE channel=$channel")
                    ws.send(Frame.Text(subscribeCmd))
                }

                msg.subscribeResult != null -> {
                    Napier.w(
                        tag = TAG,
                        message = "╠══ SUBSCRIBE OK: recoverable=${msg.subscribeResult.recoverable} ← READY"
                    )
                }

                msg.push != null -> handlePush(msg.push, conversationId, producer)

                msg.error != null -> {
                    Napier.e(
                        tag = TAG,
                        message = "╠══ SERVER ERROR: code=${msg.error.code} msg=${msg.error.message}"
                    )
                }

                else -> {
                    Napier.w(tag = TAG, message = "╠══ UNKNOWN frame (id=${msg.id}): $line")
                }
            }
        }
    }

    private suspend fun handlePush(
        push: CentrifugoPush,
        conversationId: String,
        producer: ProducerScope<ChatMessageModel>,
    ) {
        val rawData = push.pub?.data
        if (rawData == null) {
            Napier.w(tag = TAG, message = "╠══ PUSH: pub.data is null — skip")
            return
        }

        // Payload может быть {"kind":"chat_message","data":{...}} — парсим вложенный data
        val messageElement = (rawData as? JsonObject)?.get("data") ?: rawData

        val model = runCatching {
            json.decodeFromJsonElement(MessageRemoteDto.serializer(), messageElement)
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
                model.text?.take(80)
            }"
        )

        val msgConvId = model.conversationId.toString()
        if (msgConvId != conversationId) {
            Napier.w(
                tag = TAG,
                message = "╠══ PUSH filtered: msgConvId=$msgConvId != screenConvId=$conversationId"
            )
            return
        }

        Napier.w(tag = TAG, message = "╠══ PUSH ✓ EMITTING id=${model.id}")
        producer.send(model.toModel())
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    private suspend fun fetchSubscriptionTokens(): SubscriptionTokenData {
        val url = BuildKonfig.BASE_URL + "/api/conversations/obtain_subscription_token"
        Napier.w(tag = TAG, message = "╠══ GET $url")
        val response = httpClient.get(url)
        val statusCode = response.status.value
        val body = response.bodyAsText()
        Napier.w(tag = TAG, message = "╠══ tokens HTTP $statusCode body=${body.take(300)}")

        if (statusCode != 200) error("obtain_subscription_token returned HTTP $statusCode")

        return json.decodeFromString<SubscriptionTokenResponse>(body).data
    }

    private suspend fun fetchWsAuthToken(): String? = runCatching {
        val url = BuildKonfig.BASE_URL + "/api/auth/obtain_ws_auth_token"
        Napier.w(tag = TAG, message = "╠══ GET $url")
        val response = httpClient.get(url)
        val statusCode = response.status.value
        val body = response.bodyAsText()
        Napier.w(tag = TAG, message = "╠══ ws_auth_token HTTP $statusCode bodyLen=${body.length} keys=${(json.parseToJsonElement(body).jsonObject["data"] as? JsonObject)?.keys?.joinToString() ?: "null"}")

        if (statusCode != 200) error("obtain_ws_auth_token returned HTTP $statusCode")

        val root = json.parseToJsonElement(body).jsonObject
        // Поддержка разных структур: token (root), data.token, data.connection_token, data (string)
        fun String?.isJwt() = this != null && this.startsWith("eyJ")
        root["token"]?.jsonPrimitive?.content?.takeIf { it.isJwt() }
            ?: root["data"]?.let { data ->
                when (data) {
                    is JsonPrimitive -> data.content.takeIf { it.isJwt() }
                    is JsonObject -> {
                        data["token"]?.jsonPrimitive?.content?.takeIf { it.isJwt() }
                            ?: data["connection_token"]?.jsonPrimitive?.content?.takeIf { it.isJwt() }
                            ?: data["auth_token"]?.jsonPrimitive?.content?.takeIf { it.isJwt() }
                            ?: data["access_token"]?.jsonPrimitive?.content?.takeIf { it.isJwt() }
                            ?: data.values.firstNotNullOfOrNull { (it as? JsonPrimitive)?.content?.takeIf { c -> c.isJwt() } }
                    }
                    else -> null
                }
            }
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
    }
}
