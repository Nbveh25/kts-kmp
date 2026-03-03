package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.compose.runtime.Immutable

/* Состояние экрана входа */
@Immutable
internal data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonActive: Boolean = false,
    val error: String? = null
)
