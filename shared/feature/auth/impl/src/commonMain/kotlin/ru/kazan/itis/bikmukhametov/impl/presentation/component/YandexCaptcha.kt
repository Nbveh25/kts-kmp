package ru.kazan.itis.bikmukhametov.impl.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/*
 * Виджет Yandex Smart Captcha.
 * При успешном прохождении вызывает [onToken] с токеном для отправки на бэкенд.
 */
@Composable
expect fun YandexCaptchaWidget(
    siteKey: String,
    modifier: Modifier = Modifier,
    onToken: (String) -> Unit
)
