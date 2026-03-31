package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

@Immutable
internal sealed interface ChatRow {
    @Immutable
    data class Message(val model: ChatMessageModel, val showAvatar: Boolean) : ChatRow

    @Immutable
    data class DateHeader(val label: String, val epochDay: Long) : ChatRow
}
