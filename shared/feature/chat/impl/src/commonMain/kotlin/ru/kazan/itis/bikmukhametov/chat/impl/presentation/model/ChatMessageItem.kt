package ru.kazan.itis.bikmukhametov.chat.impl.presentation.model

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType

/* Модель сообщения для UI */
internal data class ChatMessageItem (
    val id: String,
    val text: String,
    val sender: MessageSender,
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
    )
}
