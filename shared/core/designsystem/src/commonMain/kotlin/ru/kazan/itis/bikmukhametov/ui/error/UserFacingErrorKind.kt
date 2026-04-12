package ru.kazan.itis.bikmukhametov.ui.error

/**
 * Классификация «сырого» [Throwable.message] / текста с API для показа на [ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen].
 */
internal enum class UserFacingErrorKind {
    /** Подставить общий текст из ресурсов. */
    UseDefault,

    /** JSON не совпал со схемой приложения (новые поля и т.п.). */
    JsonSchemaMismatch,

    /** Сеть / DNS / соединение. */
    Network,

    /** Таймаут. */
    Timeout,

    /** TLS / сертификат. */
    Ssl,

    /** 401 / не авторизован. */
    Unauthorized,

    /** Показать короткое сообщение как есть (например, текст от сервера). */
    ShowAsIs,
}

internal fun classifyUserError(raw: String?): UserFacingErrorKind {
    if (raw.isNullOrBlank()) return UserFacingErrorKind.UseDefault
    val m = raw
    return when {
        m.contains("Unknown key", ignoreCase = true) -> UserFacingErrorKind.JsonSchemaMismatch
        m.contains("ignoreUnknownKeys", ignoreCase = true) -> UserFacingErrorKind.JsonSchemaMismatch
        m.contains("JsonDecodingException", ignoreCase = true) -> UserFacingErrorKind.JsonSchemaMismatch
        m.contains("SerializationException", ignoreCase = true) -> UserFacingErrorKind.JsonSchemaMismatch
        m.contains("SerializerMissingFieldException", ignoreCase = true) -> UserFacingErrorKind.JsonSchemaMismatch
        m.contains("Polymorphic serializer", ignoreCase = true) -> UserFacingErrorKind.JsonSchemaMismatch
        m.contains("Field '", ignoreCase = true) && m.contains("required", ignoreCase = true) ->
            UserFacingErrorKind.JsonSchemaMismatch

        m.contains("SocketTimeoutException", ignoreCase = true) -> UserFacingErrorKind.Timeout
        m.contains("Connect timeout", ignoreCase = true) -> UserFacingErrorKind.Timeout
        m.contains("Read timed out", ignoreCase = true) -> UserFacingErrorKind.Timeout

        m.contains("Unable to resolve host", ignoreCase = true) -> UserFacingErrorKind.Network
        m.contains("No address associated with hostname", ignoreCase = true) -> UserFacingErrorKind.Network
        m.contains("Connection refused", ignoreCase = true) -> UserFacingErrorKind.Network
        m.contains("Network is unreachable", ignoreCase = true) -> UserFacingErrorKind.Network
        m.contains("Failed to connect", ignoreCase = true) -> UserFacingErrorKind.Network

        m.contains("HTTP 401", ignoreCase = true) -> UserFacingErrorKind.Unauthorized
        m.contains("401 Unauthorized", ignoreCase = true) -> UserFacingErrorKind.Unauthorized
        m.contains("403 Forbidden", ignoreCase = true) -> UserFacingErrorKind.Unauthorized

        m.contains("SSL", ignoreCase = true) && m.contains("Exception", ignoreCase = true) ->
            UserFacingErrorKind.Ssl
        m.contains("CertPathValidatorException", ignoreCase = true) -> UserFacingErrorKind.Ssl

        m.length <= 220 && !looksLikeStackTrace(m) -> UserFacingErrorKind.ShowAsIs

        else -> UserFacingErrorKind.UseDefault
    }
}

private fun looksLikeStackTrace(m: String): Boolean =
    m.contains("at kotlinx.", ignoreCase = true) ||
        m.contains("at io.ktor.", ignoreCase = true) ||
        m.contains("at android.", ignoreCase = true) ||
        m.contains("at java.", ignoreCase = true) ||
        m.contains("Caused by:", ignoreCase = true) ||
        m.contains("\tat ", ignoreCase = true) ||
        (m.contains("offset ", ignoreCase = true) && m.contains("path:", ignoreCase = true))

internal fun truncateForUser(raw: String, maxLen: Int = 400): String =
    if (raw.length <= maxLen) raw else raw.take(maxLen).trimEnd() + "…"
