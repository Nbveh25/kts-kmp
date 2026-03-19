package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ GET /api/conversations/obtain_subscription_token.
 *   token              — connection JWT для Centrifugo connect command.
 *   subscription_token — channel JWT для Centrifugo subscribe command.
 */
@Serializable
data class SubscriptionTokenResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: SubscriptionTokenData,
)

@Serializable
data class SubscriptionTokenData(
    @SerialName("token") val connectionToken: String? = null,
    @SerialName("subscription_token") val subscriptionToken: String,
)
