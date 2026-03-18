package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType

@Serializable
data class ChatMessageResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: MessageData
)

@Serializable
data class MessageData(
    @SerialName("messages") val messages: List<MessageRemoteModel>
)

@Serializable
data class MessageRemoteModel(
    @SerialName("id") val id: String,
    @SerialName("conversation_id") val conversationId: Long,
    @SerialName("text") val text: String,
    @SerialName("kind") val kind: String, // "user", "bot", "service"
    @SerialName("date_created") val dateCreated: String,
    @SerialName("manager_email") val managerEmail: String? = null,
    @SerialName("block_id") val blockId: String? = null,
    @SerialName("scenario_id") val scenarioId: String? = null,
    @SerialName("bucket") val bucket: String? = null
)

internal fun MessageRemoteModel.toModel(): ChatMessageModel {
    val senderType = when (kind.lowercase()) {
        "user" -> SenderType.USER
        "bot" -> SenderType.BOT
        "service" -> SenderType.SERVICE
        else -> SenderType.UNKNOWN
    }

    return ChatMessageModel(
        id = id,
        text = text,
        senderType = senderType,
        createdAt = dateCreated,
        managerEmail = managerEmail
    )
}
