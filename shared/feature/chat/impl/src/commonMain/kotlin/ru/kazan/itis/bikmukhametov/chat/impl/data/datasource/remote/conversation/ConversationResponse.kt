package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ответ API get_conversation: { "status", "data": { поля диалога (id, user, channel, ...) } } */
@Serializable
data class GetConversationApiResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: ConversationDto? = null
)

@Serializable
data class ConversationDto(
    @SerialName("id") val id: Long = 0L,
    @SerialName("conversation_id") val conversationId: Long = 0L,
    @SerialName("user") val user: UserDto? = null,
    @SerialName("participant") val participant: UserDto? = null,
    @SerialName("channel") val channel: ChannelDto? = null,
    @SerialName("state") val state: ConversationStateDto? = null,
    @SerialName("last_message") val lastMessage: MessageDto? = null,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("date_updated") val dateUpdated: String = ""
)

@Serializable
data class UserDto(
    @SerialName("_id") val id: String = "",
    @SerialName("username") val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("photo") val photo: PhotoDto? = null,
    @SerialName("url") val profileUrl: String? = null
)

@Serializable
data class PhotoDto(
    @SerialName("url") val url: String? = null
)

@Serializable
data class ChannelDto(
    @SerialName("_id") val id: String = "",
    @SerialName("kind") val kind: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("url") val url: String = ""
)

@Serializable
data class ConversationStateDto(
    @SerialName("stopped_by_manager") val stoppedByManager: Boolean = false,
    @SerialName("operator_tagged") val operatorTagged: Boolean = false,
    @SerialName("has_unanswered_operator_message") val hasUnansweredOperatorMessage: Boolean = false
)

@Serializable
data class MessageDto(
    @SerialName("id") val id: String = "",
    @SerialName("conversation_id") val conversationId: Long = 0L,
    @SerialName("text") val text: String? = null,
    @SerialName("kind") val kind: String = "text",
    @SerialName("manager_email") val managerEmail: String? = null,
    @SerialName("date_created") val dateCreated: String = ""
)
