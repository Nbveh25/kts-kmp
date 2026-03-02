package ru.kazan.itis.bikmukhametov.impl.presentation.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
import ru.kazan.itis.bikmukhametov.theme.Spacing
import kotlinx.coroutines.flow.collect

/* Экран логина */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState(initial = LoginUiState())

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

        /* Ввод логина */
        AppTextField(
            value = state.username,
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

        Spacer(modifier = Modifier.height(Spacing.paddingLarge))

        /* Кнопка отправить */
        Button(
            onClick = { viewModel.onAction(LoginAction.Submit) },
            enabled = state.isLoginButtonActive,
            modifier = Modifier.fillMaxWidth().height(Dimensions.buttonHeight),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(Res.string.login_signin),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
