package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

internal sealed interface ChatRow {
    data class Message(val model: ChatMessageModel) : ChatRow
    data class DateHeader(val label: String, val epochDay: Long) : ChatRow
}
