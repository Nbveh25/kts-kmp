package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    @SerialName("conversation_id") val conversationId: Long,
    @SerialName("message_text") val messageText: String,
    @SerialName("attachments") val attachments: List<String> = emptyList()
)
