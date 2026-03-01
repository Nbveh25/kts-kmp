package ru.kazan.itis.bikmukhametov.impl.presentation.screen

/* Состояние экрана входа */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonActive: Boolean = false,
    val error: String? = null
)
