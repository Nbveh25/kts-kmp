package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.compose.runtime.Immutable

/* Состояние экрана входа */
@Immutable
internal data class LoginUiState(
    val email: String = "tima.bikmukhametov@inbox.ru",
    val password: String = "kts2005!",
    val captchaToken: String = "",
    val isLoginButtonActive: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    /** Увеличивается при ошибке логина — пересоздаём виджет капчи для нового токена (токены одноразовые). */
    val captchaWidgetKey: Int = 0
)
