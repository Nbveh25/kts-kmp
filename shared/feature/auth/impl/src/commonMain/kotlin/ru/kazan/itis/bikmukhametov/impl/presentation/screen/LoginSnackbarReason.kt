package ru.kazan.itis.bikmukhametov.impl.presentation.screen

/** Тип сообщения для SnackBar после неудачного входа (текст подставляет UI из strings). */
internal enum class LoginSnackbarReason {
    None,
    InvalidCredentials,
    SessionNotConfirmed,
    Generic,
}
