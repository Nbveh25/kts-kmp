package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartBotRequest(
    @SerialName("conversation_id") val conversationId: Long,
    @SerialName("block_id") val blockId: String? = null,
)

@Serializable
data class StopBotRequest(
    @SerialName("conversation_id") val conversationId: Long,
)
