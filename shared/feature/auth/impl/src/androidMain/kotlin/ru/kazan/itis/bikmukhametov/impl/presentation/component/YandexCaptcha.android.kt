package ru.kazan.itis.bikmukhametov.impl.presentation.component

import android.os.Handler
import androidx.compose.ui.viewinterop.AndroidView
import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun YandexCaptchaWidget(
    siteKey: String,
    modifier: Modifier,
    onToken: (String) -> Unit
) {
    // Формируем HTML страницу с капчей
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <script src="https://smartcaptcha.yandexcloud.net/captcha.js" defer></script>
            <script>
                // Эта функция будет вызвана при успешном прохождении капчи
                function onSmartCaptchaToken(token) {
                    // Обращаемся к интерфейсу, проброшенному из Kotlin
                    AndroidCallback.onToken(token);
                }
            </script>
        </head>
        <body style="margin: 0; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: transparent;">
            <div id="captcha-container"
                 class="smart-captcha"
                 data-sitekey="$siteKey"
                 data-callback="onSmartCaptchaToken">
            </div>
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        modifier = modifier.height(150.dp),
        factory = { context ->
            WebView(context).apply {
                // Обязательно включаем JS для работы капчи
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                // User-Agent как в запросе логина — сервер может сверять токен капчи с запросом
                settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0"

                // Настраиваем прозрачный фон, если нужно гармонично вписать в UI
                setBackgroundColor(android.graphics.Color.TRANSPARENT)

                // Добавляем интерфейс-мост между JavaScript и Kotlin
                addJavascriptInterface(object : Any() {
                    @JavascriptInterface
                    fun onToken(token: String) {
                        // Передаем токен обратно в Compose
                        onToken(token)
                    }
                }, "AndroidCallback")

                webViewClient = WebViewClient()

                // ВАЖНО: Вместо "https://yourdomain.com" укажите домен,
                // который вы разрешили в настройках Яндекс Капчи (в консоли Yandex Cloud)
                val baseUrl = "https://auth.smartbotpro.ru"

                loadDataWithBaseURL(
                    baseUrl,
                    htmlContent,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}


