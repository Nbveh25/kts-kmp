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
    val dateUpdated: String
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
    val kind: ChannelKind,
    val name: String?,
    val url: String?
)

/**
 * Типы каналов (можно расширять по мере необходимости)
 */
enum class ChannelKind(val apiValue: String, val displayName: String) {
    JIVO("jivo", "JivoChat"),
    MAX("max", "MAX"),
    TG("tg", "Telegram"),
    VB("vb", "Viber"),
    WZ("wz", "Wazzup24"),
    WIDGET("widget", "Виджет"),
    VK("vk", "ВКонтакте"),
    //WA("wa", "WhatsApp"),
    //AVITO("avito", "Авито"),
    //API("api", "API"),
    //CQ("cq", "Carrot Quest"),
    //OZON("ozon", "Ozon"),
    //WB("wb", "Wildberries"),
    //OK("ok", "Одноклассники"),
    UNKNOWN("unknown", "Неизвестно");

    companion object {
        fun fromString(value: String?): ChannelKind {
            val normalized = value?.lowercase() ?: return UNKNOWN
            return entries.find { it.apiValue == normalized } ?: UNKNOWN
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
    val dateCreated: String,
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
