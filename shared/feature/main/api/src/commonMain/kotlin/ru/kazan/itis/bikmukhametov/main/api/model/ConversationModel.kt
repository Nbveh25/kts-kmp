package ru.kazan.itis.bikmukhametov.main.api.model

/**
 * Модель диалога (чата) для бизнес-логики и UI
 */
data class ConversationModel(
    val id: Long,
    val user: UserModel,
    val channel: ChannelModel,
    val state: ConversationStateModel,
    val isRead: Boolean,
    val lastMessage: LastMessageModel?,
    val dateUpdated: String // или Instant, если парсинг будет позже
)

/**
 * Пользователь в чате
 */
data class UserModel(
    val id: String,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val photo: PhotoModel?,
    val url: String?
)

/**
 * Фотография пользователя
 */
data class PhotoModel(
    val url: String?
)

/**
 * Канал (источник чата)
 */
data class ChannelModel(
    val id: String,
    val kind: ChannelKind, // enum для строгой типизации
    val name: String?,
    val url: String?
)

/**
 * Типы каналов (можно расширять по мере необходимости)
 */
enum class ChannelKind {
    TG, WA, UNKNOWN;

    companion object {
        fun fromString(value: String): ChannelKind = when (value.lowercase()) {
            "tg" -> TG
            "wa" -> WA
            else -> UNKNOWN
        }
    }
}

/**
 * Состояние диалога
 */
data class ConversationStateModel(
    val stoppedByManager: Boolean,
    val operatorTagged: Boolean,
    val hasUnansweredOperatorMessage: Boolean
)

/**
 * Последнее сообщение в диалоге
 */
data class LastMessageModel(
    val id: String,
    val conversationId: Long,
    val text: String?,
    val kind: MessageKind?,
    val blockId: String?,
    val scenarioId: String?,
    val bucket: String?,
    val dateCreated: String, // или Instant
    val attachments: AttachmentModel?,
    val managerEmail: String?,
    val isRead: Boolean?,
    val extra: Map<String, Any>? // В DTO указано Map<String, String>?, но может быть any
)

/**
 * Тип сообщения (от кого)
 */
enum class MessageKind {
    BOT, USER, SERVICE, UNKNOWN;

    companion object {
        fun fromString(value: String?): MessageKind? = when (value?.lowercase()) {
            "bot" -> BOT
            "user" -> USER
            "service" -> SERVICE
            else -> UNKNOWN
        }
    }
}

/**
 * Вложение в сообщении
 */
data class AttachmentModel(
    val asDocument: Boolean?,
    val filename: String?,
    val previewUrl: String?,
    val size: Int?,
    val type: String?,
    val url: String?,
    val width: Int?,
    val height: Int?
)
