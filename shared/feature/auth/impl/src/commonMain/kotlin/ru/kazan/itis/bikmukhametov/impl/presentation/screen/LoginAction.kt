package ru.kazan.itis.bikmukhametov.impl.presentation.screen

/* Интенты экрана логин */
internal sealed interface LoginAction {
    data class OnUsernameChanged(val email: String) : LoginAction
    data class OnPasswordChanged(val password: String) : LoginAction
    data class OnCaptchaTokenReceived(val token: String) : LoginAction
    data object Submit : LoginAction
}
