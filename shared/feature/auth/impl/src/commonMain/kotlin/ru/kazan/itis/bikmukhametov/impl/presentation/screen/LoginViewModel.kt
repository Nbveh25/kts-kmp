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
            is LoginAction.OnUsernameChanged -> updateUsername(action.email)
            is LoginAction.OnPasswordChanged -> updatePassword(action.password)
            is LoginAction.OnCaptchaTokenReceived -> updateCaptchaToken(action.token)
            is LoginAction.Submit -> tryLogin()
        }
    }

    private fun isFormValid(email: String, password: String, captchaToken: String) =
        email.isNotBlank() && password.isNotBlank() && captchaToken.isNotBlank()

    private fun updateUsername(email: String) {
        updateState {
            copy(
                email = email,
                isLoginButtonActive = isFormValid(email, password, captchaToken),
                error = null
            )
        }
    }

    private fun updatePassword(password: String) {
        updateState {
            copy(
                password = password,
                isLoginButtonActive = isFormValid(email, password, captchaToken),
                error = null
            )
        }
    }

    private fun updateCaptchaToken(token: String) {
        updateState {
            copy(
                captchaToken = token,
                isLoginButtonActive = isFormValid(email, password, token),
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
                email = current.email,
                password = current.password,
                captchaToken = current.captchaToken
            ).onSuccess {
                _events.emit(LoginUiEvent.LoginSuccessEvent) // навигация на экран main
            }.onFailure { error ->
                // Токен капчи одноразовый; сбрасываем и пересоздаём виджет (captchaWidgetKey)
                updateState {
                    copy(
                        error = error.message,
                        captchaToken = "",
                        captchaWidgetKey = captchaWidgetKey + 1
                    )
                }
            }

            updateState { copy(isLoading = false) }
        }
    }
}
