package ru.kazan.itis.bikmukhametov.impl.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Подставьте ключ из консоли Yandex Cloud (Smart Captcha) или передайте через BuildConfig. */
const val YANDEX_CAPTCHA_SITE_KEY_PLACEHOLDER = "ysc1_c0CEymymw31lzqzr7YrBD8FVjGpcKUPxqz9NmlL44add877c"

/**
 * Виджет Yandex Smart Captcha.
 * При успешном прохождении вызывает [onToken] с токеном для отправки на бэкенд.
 *
 * @param siteKey клиентский ключ из консоли Yandex Cloud (Smart Captcha)
 * @param onToken callback с токеном после прохождения капчи
 */
@Composable
expect fun YandexCaptchaWidget(
    siteKey: String,
    modifier: Modifier = Modifier,
    onToken: (String) -> Unit
)
