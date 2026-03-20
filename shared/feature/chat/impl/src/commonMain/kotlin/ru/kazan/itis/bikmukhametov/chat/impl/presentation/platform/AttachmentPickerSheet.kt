package ru.kazan.itis.bikmukhametov.chat.impl.presentation.platform

import androidx.compose.runtime.Composable
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment

@Composable
expect fun AttachmentPickerSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onPicked: (PickedAttachment) -> Unit,
)
