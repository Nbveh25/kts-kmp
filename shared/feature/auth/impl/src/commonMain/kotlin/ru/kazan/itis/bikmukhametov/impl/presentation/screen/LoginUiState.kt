package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.compose.runtime.Immutable

/* Состояние экрана входа */
@Immutable
internal data class LoginUiState(
    val email: String = "tima.bikmukhametov@inbox.ru", // TODO(потом убрать надо)
    val password: String = "kts2005!",
    val captchaToken: String = "",
    val emailError: EmailFieldError? = null,
    val passwordError: PasswordFieldError? = null,
    val isLoginButtonActive: Boolean = false,
    val isLoading: Boolean = false,
    val captchaWidgetKey: Int = 0, // для пересоздания токена капчи
    /** Увеличивается при каждой ошибке входа — триггер SnackBar. */
    val loginSnackbarSignal: Int = 0,
    val loginSnackbarReason: LoginSnackbarReason = LoginSnackbarReason.None,
    /** Для [LoginSnackbarReason.Generic] — текст с сервера или дефолт. */
    val loginSnackbarGenericText: String = "",
)
