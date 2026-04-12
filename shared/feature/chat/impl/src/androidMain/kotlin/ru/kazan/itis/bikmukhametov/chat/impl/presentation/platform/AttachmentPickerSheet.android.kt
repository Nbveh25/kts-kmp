package ru.kazan.itis.bikmukhametov.chat.impl.presentation.platform

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kazan.itis.bikmukhametov.chat.impl.R
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment

@Composable
actual fun AttachmentPickerSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onPicked: (PickedAttachment) -> Unit,
) {
    val context = LocalContext.current
    // LocalContext в приложении часто — createConfigurationContext (AppLocaleBridge), по нему
    // нельзя дойти до Activity. LocalActivityResultRegistryOwner иногда не прокинут в CMP;
    // у AndroidComposeView контекст обычно остаётся ComponentActivity.
    val registryOwner = LocalActivityResultRegistryOwner.current
        ?: LocalView.current.context.findActivityResultRegistryOwner()
        ?: return

    CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
        var sendAsFile by remember { mutableStateOf(false) }

        val pickMedia = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            uri?.let { readPickedUri(context, it, sendAsFile)?.let(onPicked) }
            onDismiss()
        }

        val pickFile = rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument(),
        ) { uri: Uri? ->
            uri?.let { readPickedUri(context, it, sendAsFile)?.let(onPicked) }
            onDismiss()
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.chat_attachment),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = sendAsFile,
                            onCheckedChange = { sendAsFile = it },
                        )
                        Text(
                            text = stringResource(R.string.chat_send_as_file),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.chat_photo_or_video))
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { pickFile.launch(arrayOf("*/*")) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.chat_file))
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.chat_cancel))
                    }
                }
            }
        }
    }
}

private tailrec fun Context.findActivityResultRegistryOwner(): ActivityResultRegistryOwner? =
    when (this) {
        is ActivityResultRegistryOwner -> this
        is ContextWrapper -> {
            val next = baseContext
            if (next == null || next == this) null else next.findActivityResultRegistryOwner()
        }
        else -> null
    }

private fun readPickedUri(context: Context, uri: Uri, sendAsFile: Boolean): PickedAttachment? {
    val resolver = context.contentResolver
    val mime = resolver.getType(uri)
    val meta = resolver.readAttachmentMeta(uri)
    // Не отбрасываем файл при SIZE = 0 / null: часть провайдеров так отвечает, пока размер неизвестен.
    return PickedAttachment(
        contentUri = uri.toString(),
        fileName = meta.displayName ?: "attachment",
        mimeType = mime,
        sendAsFile = sendAsFile,
        contentLength = meta.size,
    )
}

private fun android.content.ContentResolver.readAttachmentMeta(uri: Uri): AttachmentMeta {
    query(uri, null, null, null, null)?.use { cursor ->
        if (!cursor.moveToFirst()) return AttachmentMeta(displayName = null, size = null)
        val displayName = cursor.valueOrNull(OpenableColumns.DISPLAY_NAME)
        val size = cursor.longValueOrNull(OpenableColumns.SIZE)?.takeIf { it >= 0L }
        return AttachmentMeta(displayName = displayName, size = size)
    }
    return AttachmentMeta(displayName = null, size = null)
}

private fun android.database.Cursor.valueOrNull(column: String): String? {
    val idx = getColumnIndex(column)
    return if (idx >= 0) getString(idx) else null
}

private fun android.database.Cursor.longValueOrNull(column: String): Long? {
    val idx = getColumnIndex(column)
    return if (idx >= 0) getLong(idx) else null
}
