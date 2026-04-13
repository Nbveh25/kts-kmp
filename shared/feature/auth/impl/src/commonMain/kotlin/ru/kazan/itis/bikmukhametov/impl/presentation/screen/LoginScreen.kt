package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.kazan.itis.bikmukhametov.auth.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.theme.Dimensions
import ru.kazan.itis.bikmukhametov.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_error_generic
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_example_mail
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_invalid_credentials
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_login_hint
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_session_not_confirmed
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_signin
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_title
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_validation_email_invalid
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_validation_password_length
import ru.kazan.itis.bikmukhametov.impl.presentation.component.AppTextField
import ru.kazan.itis.bikmukhametov.impl.presentation.component.AuthFormFieldErrorColor
import ru.kazan.itis.bikmukhametov.impl.presentation.component.PasswordTextField
import ru.kazan.itis.bikmukhametov.impl.presentation.component.YandexCaptchaWidget
import ru.kazan.itis.bikmukhametov.theme.Spacing

/* Экран логина */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var lastConsumedSnackbarSignal by rememberSaveable { mutableIntStateOf(0) }

    val invalidCredentials = stringResource(Res.string.login_invalid_credentials)
    val sessionNotConfirmed = stringResource(Res.string.login_session_not_confirmed)
    val errorGeneric = stringResource(Res.string.login_error_generic)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> onLoginSuccess()
            }
        }
    }

    LaunchedEffect(state.loginSnackbarSignal) {
        val signal = state.loginSnackbarSignal
        if (signal == 0 || signal <= lastConsumedSnackbarSignal) return@LaunchedEffect
        val text = when (state.loginSnackbarReason) {
            LoginSnackbarReason.InvalidCredentials -> invalidCredentials
            LoginSnackbarReason.SessionNotConfirmed -> sessionNotConfirmed
            LoginSnackbarReason.Generic ->
                state.loginSnackbarGenericText.takeIf { it.isNotBlank() } ?: errorGeneric
            LoginSnackbarReason.None -> null
        } ?: return@LaunchedEffect
        lastConsumedSnackbarSignal = signal
        snackbarHostState.showSnackbar(
            message = text,
            withDismissAction = true
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
                .padding(
                    horizontal = Spacing.horizontalScreenPadding,
                    vertical = Spacing.verticalScreenPadding
                )
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(Res.string.login_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(Spacing.paddingLarge))

            AppTextField(
                value = state.email,
                onValueChange = {
                    viewModel.onAction(LoginAction.OnUsernameChanged(it))
                },
                label = stringResource(Res.string.login_login_hint),
                placeholder = stringResource(Res.string.login_example_mail),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.emailError != null,
                supportingText = if (state.emailError != null) {
                    {
                        Text(
                            text = stringResource(Res.string.login_validation_email_invalid),
                            color = AuthFormFieldErrorColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    null
                }
            )

            Spacer(modifier = Modifier.height(Spacing.paddingMedium))

            PasswordTextField(
                password = state.password,
                onPasswordChange = {
                    viewModel.onAction(LoginAction.OnPasswordChanged(it))
                },
                modifier = Modifier.padding(top = Spacing.paddingMedium),
                isError = state.passwordError != null,
                supportingText = if (state.passwordError != null) {
                    {
                        Text(
                            text = stringResource(Res.string.login_validation_password_length),
                            color = AuthFormFieldErrorColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    null
                }
            )

            Spacer(modifier = Modifier.height(Spacing.paddingMedium))

            key(state.captchaWidgetKey) {
                YandexCaptchaWidget(
                    siteKey = BuildKonfig.YANDEX_CAPTCHA_SITE_KEY,
                    onToken = { viewModel.onAction(LoginAction.OnCaptchaTokenReceived(it)) }
                )
            }

            Spacer(modifier = Modifier.height(Spacing.paddingMedium))

            Button(
                onClick = { viewModel.onAction(LoginAction.Submit) },
                enabled = state.isLoginButtonActive && !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(Dimensions.buttonHeight),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = stringResource(Res.string.login_signin),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
