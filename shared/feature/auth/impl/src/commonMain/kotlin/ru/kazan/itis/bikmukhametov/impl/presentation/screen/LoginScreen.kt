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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.kazan.itis.bikmukhametov.theme.Dimensions
import ru.kazan.itis.bikmukhametov.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_example_mail
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_login_hint
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_signin
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_title
import ru.kazan.itis.bikmukhametov.impl.presentation.component.AppTextField
import ru.kazan.itis.bikmukhametov.impl.presentation.component.PasswordTextField
import ru.kazan.itis.bikmukhametov.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.impl.presentation.component.YandexCaptchaWidget
import ru.kazan.itis.bikmukhametov.theme.Spacing

/* Экран логина */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> onLoginSuccess()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = Spacing.horizontalScreenPadding,
                vertical = Spacing.verticalScreenPadding
            )
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        /* Заголовок логина */
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Spacing.paddingLarge))

        /* Ввод email */
        AppTextField(
            value = state.email,
            onValueChange = {
                viewModel.onAction(LoginAction.OnUsernameChanged(it))
            },
            label = stringResource(Res.string.login_login_hint),
            placeholder = stringResource(Res.string.login_example_mail),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(Spacing.paddingMedium))

        /* Ввод пароля */
        PasswordTextField(
            password = state.password,
            onPasswordChange = {
                viewModel.onAction(LoginAction.OnPasswordChanged(it))
            },
            modifier = Modifier.padding(top = Spacing.paddingMedium)
        )

        /* Сообщение об ошибке */
        state.error?.let { error ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.paddingSmall),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.paddingMedium))

        /* Yandex Smart Captcha: при успехе токен уходит в state. 
        key() пересоздаёт виджет после ошибки (токен одноразовый). */
        key(state.captchaWidgetKey) {
            YandexCaptchaWidget(
                siteKey = BuildKonfig.YANDEX_CAPTCHA_SITE_KEY,
                onToken = { viewModel.onAction(LoginAction.OnCaptchaTokenReceived(it)) }
            )
        }

        Spacer(modifier = Modifier.height(Spacing.paddingMedium))

        /* Кнопка отправить */
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
