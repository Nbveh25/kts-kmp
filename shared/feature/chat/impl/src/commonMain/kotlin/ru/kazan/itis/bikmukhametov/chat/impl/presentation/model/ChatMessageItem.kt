package ru.kazan.itis.bikmukhametov.chat.impl.presentation.model

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType
import ru.kazan.itis.bikmukhametov.ui.util.epochDayOf
import ru.kazan.itis.bikmukhametov.ui.util.formatTimeOnly

/* Модель сообщения для UI */
internal data class ChatMessageItem(
    val id: String,
    val text: String,
    val sender: MessageSender,
    val createdAt: String,   // время в формате "HH:MM"
    val epochDay: Long,      // день (epochMs / 86_400_000) для группировки по датам
)

internal fun ChatMessageModel.toItem(): ChatMessageItem {
    val sender = when (senderType) {
        SenderType.USER -> MessageSender.CLIENT
        SenderType.BOT -> MessageSender.OPERATOR
        SenderType.SERVICE -> MessageSender.SYSTEM
        SenderType.UNKNOWN -> MessageSender.SYSTEM
    }

    return ChatMessageItem(
        id = id,
        text = text,
        sender = sender,
        createdAt = formatTimeOnly(createdAt),
        epochDay = epochDayOf(createdAt),
    )
}
