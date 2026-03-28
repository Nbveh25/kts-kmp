package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment

internal sealed interface ChatAction {
    data object Refresh : ChatAction
    data object ListEndReached : ChatAction
    data class OnMessageTextChange(val text: String) : ChatAction
    data object OnSendMessageClick : ChatAction
    data object OnBotToggleClick : ChatAction
    data class OnMenuExpandChange(val expanded: Boolean) : ChatAction

    data object OnOpenAttachmentPicker : ChatAction
    data object OnAttachmentPickerDismiss : ChatAction
    data class OnAttachmentPicked(val attachment: PickedAttachment) : ChatAction
    data object OnClearPendingAttachment : ChatAction
}
