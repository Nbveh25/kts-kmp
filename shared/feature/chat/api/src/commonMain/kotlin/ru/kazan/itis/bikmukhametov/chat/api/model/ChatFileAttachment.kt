package ru.kazan.itis.bikmukhametov.chat.api.model

/**
 * Неизображение (документ и т.п.): имя, размер и URL для открытия в браузере/viewer.
 * [openUrl] может быть null, если в ответе нет ни ссылки, ни id вложения.
 */
data class ChatFileAttachment(
    val fileName: String,
    val sizeBytes: Int? = null,
    val openUrl: String? = null,
)
