package ru.kazan.itis.bikmukhametov.kts.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kts.composeapp.generated.resources.Res
import kts.composeapp.generated.resources.example_mail
import kts.composeapp.generated.resources.login
import kts.composeapp.generated.resources.signin
import kts.composeapp.generated.resources.title_login
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.kts.presentation.component.AppTextField
import ru.kazan.itis.bikmukhametov.kts.presentation.component.PasswordTextField

@Composable
fun LoginScreen() {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Тайтл
        Text(
            text = stringResource(Res.string.title_login),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Поле ввода логина
        AppTextField(
            value = login,
            onValueChange = { login = it },
            label = stringResource(Res.string.login),
            placeholder = stringResource(Res.string.example_mail),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода пароля
        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка входа
        Button(
            onClick = {
                // Логика в следующей домашке
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(Res.string.signin),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
