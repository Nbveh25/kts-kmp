package ru.kazan.itis.bikmukhametov.chat.impl.data.platform

import io.ktor.utils.io.ByteReadChannel

internal class IosAttachmentContentReader : AttachmentContentReader {
    override fun openReadChannel(contentUri: String): ByteReadChannel? = null
    override suspend fun queryContentLength(contentUri: String): Long? = null
}
