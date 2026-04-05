package ru.kazan.itis.bikmukhametov.chat.api.model

data class ChatMessageModel(
    val id: String,
    val text: String,
    val senderType: SenderType,
    val createdAt: String,
    val managerEmail: String? = null,
    /** Абсолютные URL превью изображений из вложений сообщения (для отображения в чате). */
    val imageAttachmentUrls: List<String> = emptyList(),
)

enum class SenderType {
    USER,    // Сообщение от клиента
    BOT,     // Ответ автоматики
    SERVICE, // Системные события (stop_bot, start_bot)
    UNKNOWN
}
