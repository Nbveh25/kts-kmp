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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.theme.Dimensions
import ru.kazan.itis.bikmukhametov.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_example_mail
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_login_hint
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_signin
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_title
import ru.kazan.itis.bikmukhametov.impl.presentation.component.AppTextField
import ru.kazan.itis.bikmukhametov.impl.presentation.component.PasswordTextField
import ru.kazan.itis.bikmukhametov.theme.Spacing

/* Экран входа */
@Composable
fun LoginScreen() {

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

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

        // Тайтл
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Spacing.paddingLarge))

        // Поле ввода логина
        AppTextField(
            value = login,
            onValueChange = { login = it },
            label = stringResource(Res.string.login_login_hint),
            placeholder = stringResource(Res.string.login_example_mail),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(Spacing.paddingMedium))

        // Поле ввода пароля
        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            modifier = Modifier.padding(top = Spacing.paddingMedium)
        )

        Spacer(modifier = Modifier.height(Spacing.paddingLarge))

        // Кнопка входа
        Button(
            onClick = {
                // Логика в следующей домашке
            },
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
