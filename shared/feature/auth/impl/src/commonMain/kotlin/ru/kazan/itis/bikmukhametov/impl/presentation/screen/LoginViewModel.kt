package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.api.usecase.LoginUseCase
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginSnackbarReason.Generic
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginSnackbarReason.InvalidCredentials
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginSnackbarReason.SessionNotConfirmed
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginUiEvent.LoginSuccessEvent
import ru.kazan.itis.bikmukhametov.network.auth.session.SessionChecker
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

/* Вьюмодель экрана входа */
internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val sessionChecker: SessionChecker,
) : BaseViewModel<LoginUiState, LoginAction>(LoginUiState()) {

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

    private fun updateUsername(email: String) {
        updateState {
            val emailErr = LoginCredentialsRules.emailFieldError(email)
            copy(
                email = email,
                emailError = emailErr,
                isLoginButtonActive = LoginCredentialsRules.canSubmit(email, password, captchaToken)
            )
        }
    }

    private fun updatePassword(password: String) {
        updateState {
            val passErr = LoginCredentialsRules.passwordFieldError(password)
            copy(
                password = password,
                passwordError = passErr,
                isLoginButtonActive = LoginCredentialsRules.canSubmit(email, password, captchaToken)
            )
        }
    }

    private fun updateCaptchaToken(token: String) {
        updateState {
            copy(
                captchaToken = token,
                isLoginButtonActive = LoginCredentialsRules.canSubmit(email, password, token)
            )
        }
    }

    private fun pushLoginError(
        reason: LoginSnackbarReason,
        genericText: String = "",
    ) {
        updateState {
            copy(
                loginSnackbarSignal = loginSnackbarSignal + 1,
                loginSnackbarReason = reason,
                loginSnackbarGenericText = genericText,
                captchaToken = "",
                captchaWidgetKey = captchaWidgetKey + 1
            )
        }
    }

    private fun tryLogin() {
        val current = state.value

        viewModelScope.launch {
            if (!LoginCredentialsRules.canSubmit(
                    current.email,
                    current.password,
                    current.captchaToken
                )
            ) {
                updateState {
                    copy(
                        emailError = LoginCredentialsRules.emailFieldError(current.email),
                        passwordError = LoginCredentialsRules.passwordFieldError(current.password),
                        isLoginButtonActive = LoginCredentialsRules.canSubmit(
                            current.email,
                            current.password,
                            current.captchaToken
                        )
                    )
                }
                return@launch
            }

            updateState { copy(isLoading = true) }

            loginUseCase(
                email = current.email,
                password = current.password,
                captchaToken = current.captchaToken
            ).fold(
                onSuccess = {
                    val sessionOk = runCatching { sessionChecker.isSessionValid() }.getOrDefault(false)
                    if (sessionOk) {
                        _events.emit(LoginSuccessEvent)
                    } else {
                        pushLoginError(SessionNotConfirmed)
                    }
                },
                onFailure = { error ->
                    val reason = if (error.isInvalidLoginCredentials()) {
                        InvalidCredentials
                    } else {
                        Generic
                    }
                    val generic = if (reason == Generic) {
                        error.message.orEmpty()
                    } else {
                        ""
                    }
                    pushLoginError(reason, genericText = generic)
                }
            )

            updateState { copy(isLoading = false) }
        }
    }
}

private fun Throwable.isInvalidLoginCredentials(): Boolean {
    var current: Throwable? = this
    while (current != null) {
        val msg = current.message.orEmpty()
        if (msg.contains("bad", ignoreCase = true)) return true
        if (msg.contains("not_authorized", ignoreCase = true)) return true
        if (msg.contains("not authorized", ignoreCase = true)) return true
        val clientError = current as? ClientRequestException
        if (clientError != null) {
            val status = clientError.response.status
            if (status == HttpStatusCode.Unauthorized || status == HttpStatusCode.Forbidden) {
                return true
            }
        }
        current = current.cause
    }
    return false
}
