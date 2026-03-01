package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.kazan.itis.bikmukhametov.ui.util.BasicViewModel

/* Вьюмодель экрана входа */
class LoginViewModel : BasicViewModel<LoginUiState, LoginAction>(LoginUiState()) {

    private val _events = MutableSharedFlow<LoginUiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    override fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChanged -> updateUsername(action.username)
            is LoginAction.OnPasswordChanged -> updatePassword(action.password)
            is LoginAction.Submit -> tryLogin()
        }
    }

    // Ввод юзернейма
    private fun updateUsername(username: String) {
        updateState {
            val newIsActive = username.isNotBlank() && password.isNotBlank()
            copy(
                username = username,
                isLoginButtonActive = newIsActive,
                error = null
            )
        }
    }

    // Ввод пароля
    private fun updatePassword(password: String) {
        updateState {
            val newIsActive = username.isNotBlank() && password.isNotBlank()
            copy(
                password = password,
                isLoginButtonActive = newIsActive,
                error = null
            )
        }
    }

    // Авторизация
    private fun tryLogin() {
        val current = state.value
        if (!current.isLoginButtonActive) return

        // Моковая проверка: успех при user / pass
        val isValid = current.username.trim() == "user" && current.password == "pass"
        if (isValid) {
            _events.tryEmit(LoginUiEvent.LoginSuccessEvent)
        } else {
            updateState { copy(error = "Неверный логин или пароль") }
        }
    }
}
