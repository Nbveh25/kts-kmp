package ru.kazan.itis.bikmukhametov.impl.presentation.component

import androidx.compose.ui.viewinterop.AndroidView
import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
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
    val htmlContent = """
        <!DOCTYPE html>
        <html style="height: 100%; margin: 0; padding: 0;">
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <script src="https://smartcaptcha.yandexcloud.net/captcha.js" defer></script>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    min-height: 100%;
                    width: 100%;
                    display: flex;
                    justify-content: center;
                    align-items: flex-start;
                    background-color: transparent;
                    overflow: hidden;
                }
                #captcha-container {
                    width: 100%;
                    min-height: 120px;
                    padding-top: 0;
                }
            </style>
            <script>
                function onSmartCaptchaToken(token) {
                    if (window.AndroidCallback) {
                        window.AndroidCallback.onToken(token);
                    }
                }
            </script>
        </head>
        <body>
            <div id="captcha-container"
                 class="smart-captcha"
                 data-sitekey="$siteKey"
                 data-callback="onSmartCaptchaToken">
            </div>
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

                setBackgroundColor(android.graphics.Color.TRANSPARENT)

                addJavascriptInterface(object : Any() {
                    @JavascriptInterface
                    fun onToken(token: String) {
                        post { onToken(token) }
                    }
                }, "AndroidCallback")

                webViewClient = WebViewClient()

                val baseUrl = "https://auth.smartbotpro.ru"

                loadDataWithBaseURL(
                    baseUrl,
                    htmlContent,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        update = { webView ->
            webView.requestLayout()
        }
    )
}