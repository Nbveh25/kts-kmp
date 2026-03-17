package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ответ API get_conversation: { "status", "data": { поля диалога (id, user, channel, ...) } } */
@Serializable
data class GetConversationApiResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: ConversationDto
)

@Serializable
data class ConversationDto(
    @SerialName("id") val id: Long,
    @SerialName("user") val user: UserDto,
    @SerialName("channel") val channel: ChannelDto,
    @SerialName("state") val state: ConversationStateDto,
    @SerialName("last_message") val lastMessage: MessageDto?,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("date_updated") val dateUpdated: String
)

@Serializable
data class UserDto(
    @SerialName("_id") val id: String,
    @SerialName("username") val username: String?,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("photo") val photo: PhotoDto?,
    @SerialName("url") val profileUrl: String?
)

@Serializable
data class PhotoDto(
    @SerialName("url") val url: String?
)

@Serializable
data class ChannelDto(
    @SerialName("_id") val id: String,

    @SerialName("kind")
    val kind: String,

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String
)

@Serializable
data class ConversationStateDto(
    @SerialName("stopped_by_manager")
    val stoppedByManager: Boolean,

    @SerialName("operator_tagged")
    val operatorTagged: Boolean,

    @SerialName("has_unanswered_operator_message")
    val hasUnansweredOperatorMessage: Boolean
)

@Serializable
data class MessageDto(
    @SerialName("id")
    val id: String,

    @SerialName("conversation_id")
    val conversationId: Long,

    @SerialName("text")
    val text: String?,

    @SerialName("kind")
    val kind: String,

    @SerialName("manager_email")
    val managerEmail: String?,

    @SerialName("date_created")
    val dateCreated: String
)
