package ru.kazan.itis.bikmukhametov.chat.impl.data.platform

import io.ktor.utils.io.ByteReadChannel

/**
 * Открывает поток данных для content URI (Android `content://`)
 * без чтения всего файла в память.
 */
interface AttachmentContentReader {
    /**
     * Синхронно открывает поток; вызывается из Ktor при сборке multipart
     */
    fun openReadChannel(contentUri: String): ByteReadChannel?

    suspend fun queryContentLength(contentUri: String): Long?
}
