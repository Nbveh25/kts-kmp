package ru.kazan.itis.bikmukhametov.impl.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.theme.Dimensions
import ru.kazan.itis.bikmukhametov.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.impl.generated.resources.ic_vis_24
import ru.kazan.itis.bikmukhametov.impl.generated.resources.ic_vis_off_24
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_password_hint
import ru.kazan.itis.bikmukhametov.impl.generated.resources.login_password_visibility_toggle

/* Базовое поле ввода: текст или пароль (при isPassword = true — маскировка и иконка видимости) */
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val effectiveTransformation = when {
        isPassword -> if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        else -> visualTransformation
    }
    val effectiveKeyboardOptions =
        if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password)
        else keyboardOptions
    val effectiveTrailingIcon: @Composable (() -> Unit)? = when {
        isPassword -> {
            {
                IconButton(
                    modifier = Modifier.size(Dimensions.iconButtonSize),
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        modifier = Modifier.size(Dimensions.iconSize),
                        painter = painterResource(
                            if (isPasswordVisible) Res.drawable.ic_vis_24
                            else Res.drawable.ic_vis_off_24
                        ),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.login_password_visibility_toggle)
                    )
                }
            }
        }

        else -> trailingIcon
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodySmall,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        keyboardOptions = effectiveKeyboardOptions,
        visualTransformation = effectiveTransformation,
        trailingIcon = effectiveTrailingIcon,
        colors = defaultTextFieldColors()
    )
}

/* Обёртка для поля пароля с подставленным лейблом из ресурсов */
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    label: String = stringResource(Res.string.login_password_hint)
) {
    AppTextField(
        modifier = modifier,
        value = password,
        onValueChange = onPasswordChange,
        label = label,
        isPassword = true
    )
}

/* Основные цвета для полей ввода */
@Composable
private fun defaultTextFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    errorTextColor = MaterialTheme.colorScheme.error,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surface,
    errorContainerColor = MaterialTheme.colorScheme.surface,
    cursorColor = MaterialTheme.colorScheme.onSurface,
    errorCursorColor = MaterialTheme.colorScheme.error,
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
    disabledIndicatorColor = MaterialTheme.colorScheme.primary,
    errorIndicatorColor = MaterialTheme.colorScheme.error,
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
    errorLeadingIconColor = MaterialTheme.colorScheme.error,
    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
    disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
    errorTrailingIconColor = MaterialTheme.colorScheme.error,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
    disabledLabelColor = MaterialTheme.colorScheme.onSurface,
    errorLabelColor = MaterialTheme.colorScheme.error,
)
