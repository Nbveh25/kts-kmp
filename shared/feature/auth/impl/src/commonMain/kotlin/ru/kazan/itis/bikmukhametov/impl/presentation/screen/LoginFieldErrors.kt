package ru.kazan.itis.bikmukhametov.impl.presentation.screen

/** Ошибка формата e-mail (текст задаётся в strings.xml на UI). */
internal enum class EmailFieldError {
    InvalidFormat,
}

/** Пароль вне диапазона 8–256 символов. */
internal enum class PasswordFieldError {
    LengthInvalid,
}

internal object LoginCredentialsRules {
    const val PASSWORD_MIN_LENGTH = 8
    const val PASSWORD_MAX_LENGTH = 256

    /** Упрощённая проверка e-mail для UI (не RFC-полная). */
    private val emailRegex = Regex(
        "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"
    )

    fun emailFieldError(email: String): EmailFieldError? = when {
        email.isBlank() -> null
        !emailRegex.matches(email.trim()) -> EmailFieldError.InvalidFormat
        else -> null
    }

    fun passwordFieldError(password: String): PasswordFieldError? = when {
        password.isEmpty() -> null
        password.length < PASSWORD_MIN_LENGTH -> PasswordFieldError.LengthInvalid
        password.length > PASSWORD_MAX_LENGTH -> PasswordFieldError.LengthInvalid
        else -> null
    }

    fun canSubmit(email: String, password: String, captchaToken: String): Boolean =
        email.isNotBlank() &&
            password.isNotBlank() &&
            captchaToken.isNotBlank() &&
            emailFieldError(email) == null &&
            passwordFieldError(password) == null
}
