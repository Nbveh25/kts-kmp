package ru.kazan.itis.bikmukhametov.chat.impl.data.platform

import android.content.Context
import android.provider.OpenableColumns
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

internal class AndroidAttachmentContentReader(
    private val context: Context,
) : AttachmentContentReader {

    override fun openReadChannel(contentUri: String): ByteReadChannel? {
        val uri = contentUri.toUri()
        val stream = context.contentResolver.openInputStream(uri) ?: return null
        return stream.toByteReadChannel()
    }

    override suspend fun queryContentLength(contentUri: String): Long? = withContext(Dispatchers.IO) {
        val uri = contentUri.toUri()
        context.contentResolver.query(uri, null, null, null, null)?.use { c ->
            if (c.moveToFirst()) {
                val idx = c.getColumnIndex(OpenableColumns.SIZE)
                if (idx >= 0) c.getLong(idx).takeIf { it >= 0 } else null
            } else {
                null
            }
        }
    }
}
