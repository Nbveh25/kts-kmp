package ru.kazan.itis.bikmukhametov.chat.impl.presentation.model

import androidx.compose.runtime.Immutable

/**
 * Выбранное вложение: метаданные и URI контента.
 * Байты читаются только при загрузке на сервер потоком.
 */
@Immutable
data class PickedAttachment(
    val contentUri: String,
    val fileName: String,
    val mimeType: String?,
    val sendAsFile: Boolean,
    /** Известный размер файла в байтах (если контент-провайдер отдал) */
    val contentLength: Long? = null,
)
