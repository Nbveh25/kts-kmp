package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// ── Commands (client → server) ────────────────────────────────────────────────

@Serializable
data class CentrifugoConnectCommand(
    @SerialName("id") val id: Int,
    @SerialName("connect") val connect: CentrifugoConnectParams,
)

@Serializable
data class CentrifugoConnectParams(
    @SerialName("token") val token: String,
    @SerialName("name") val name: String = "",
)

@Serializable
data class CentrifugoSubscribeCommand(
    @SerialName("id") val id: Int,
    @SerialName("subscribe") val subscribe: CentrifugoSubscribeParams,
)

@Serializable
data class CentrifugoSubscribeParams(
    @SerialName("channel") val channel: String,
    @SerialName("token") val token: String? = null,
    /** Браузер отправляет flag=1 — без него сервер отвергает subscribe. */
    @SerialName("flag") val flag: Int = 1,
)

// ── Replies / Pushes (server → client) ───────────────────────────────────────

/**
 * Универсальный контейнер ответов сервера.
 * - id != null  → reply на команду (connect/subscribe).
 * - push != null → серверный push (новое сообщение, join, leave…).
 * - {}          → ping-фрейм, клиент должен ответить {}.
 */
@Serializable
data class CentrifugoServerMessage(
    @SerialName("id") val id: Int? = null,
    @SerialName("connect") val connectResult: CentrifugoConnectResult? = null,
    @SerialName("subscribe") val subscribeResult: CentrifugoSubscribeResult? = null,
    @SerialName("push") val push: CentrifugoPush? = null,
    @SerialName("error") val error: CentrifugoError? = null,
)

@Serializable
data class CentrifugoConnectResult(
    @SerialName("client") val client: String? = null,
    @SerialName("version") val version: String? = null,
)

@Serializable
data class CentrifugoSubscribeResult(
    @SerialName("recoverable") val recoverable: Boolean? = null,
)

@Serializable
data class CentrifugoPush(
    @SerialName("channel") val channel: String,
    @SerialName("pub") val pub: CentrifugoPublication? = null,
)

@Serializable
data class CentrifugoPublication(
    @SerialName("data") val data: JsonElement? = null,
)

@Serializable
data class CentrifugoError(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
)
