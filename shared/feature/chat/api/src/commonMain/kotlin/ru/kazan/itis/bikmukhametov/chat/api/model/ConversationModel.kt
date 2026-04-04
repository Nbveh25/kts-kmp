package ru.kazan.itis.bikmukhametov.chat.api.model

data class ConversationModel(
    val id: Long,
    val user: UserModel,
    val channel: ChannelModel,
    val state: ConversationStateModel,
    val lastMessage: LastMessageModel?,
    val isRead: Boolean,
    val updatedAt: String
)

data class UserModel(
    val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String?,
    val profileUrl: String?
)

data class ChannelModel(
    val id: String,
    /** Сырое значение `kind` из API (как в main: `ChannelKind.apiValue`). */
    val kind: String,
    val type: ChannelType,
    val name: String,
    val url: String
)

data class ConversationStateModel(
    val isStoppedByManager: Boolean,
    val isOperatorTagged: Boolean,
    val hasUnansweredOperatorMessage: Boolean
)

data class LastMessageModel(
    val id: String,
    val text: String,
    val type: MessageType,
    val managerEmail: String?,
    val createdAt: String
)

// Дополнительные типы для строгой типизации
enum class ChannelType {
    CHAT, EMAIL, TELEGRAM, UNKNOWN
}

enum class MessageType {
    TEXT, SYSTEM, IMAGE, UNKNOWN
}
