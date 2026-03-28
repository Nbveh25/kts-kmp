package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ GET /api/auth/obtain_ws_auth_token.
 * Возвращает JWT-токен для Centrifugo connect command.
 */
@Serializable
internal data class WsAuthTokenResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: WsAuthTokenData? = null
)

@Serializable
internal data class WsAuthTokenData(
    @SerialName("token") val token: String? = null,
    @SerialName("connection_token") val connectionToken: String? = null,
)
