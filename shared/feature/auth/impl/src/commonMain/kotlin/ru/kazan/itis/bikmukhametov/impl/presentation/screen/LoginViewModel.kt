package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.api.usecase.LoginUseCase
import ru.kazan.itis.bikmukhametov.ui.util.BasicViewModel

/* Вьюмодель экрана входа */
internal class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BasicViewModel<LoginUiState, LoginAction>(LoginUiState()) {

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

        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            loginUseCase(
                username = current.username,
                password = current.password
            ).onSuccess {
                _events.emit(LoginUiEvent.LoginSuccessEvent) // навигация на экран main
            }.onFailure { error ->
                updateState { copy(error = error.message) }
            }

            updateState { copy(isLoading = false) }
        }
    }
}
