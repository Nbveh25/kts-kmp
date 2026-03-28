package ru.kazan.itis.bikmukhametov.chat.impl.presentation.platform

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment

@Composable
actual fun AttachmentPickerSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onPicked: (PickedAttachment) -> Unit,
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("Вложения") },
        text = { Text("Выбор файлов на iOS пока не подключён.") },
    )
}
