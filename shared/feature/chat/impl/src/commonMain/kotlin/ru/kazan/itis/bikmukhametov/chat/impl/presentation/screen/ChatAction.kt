package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

internal sealed interface ChatAction {
    data object Refresh : ChatAction
    data object ListEndReached : ChatAction
    data class OnMessageTextChange(val text: String) : ChatAction
    data object OnSendMessageClick : ChatAction
    data object OnBotToggleClick : ChatAction
    data class OnMenuExpandChange(val expanded: Boolean) : ChatAction
}
