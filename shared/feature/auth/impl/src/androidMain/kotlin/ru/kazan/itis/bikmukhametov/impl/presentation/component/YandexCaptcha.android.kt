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

private const val MIME_TYPE = "text/html"
private const val ENCODING = "UTF-8"
private const val AUTH_URL = "https://auth.smartbotpro.ru"

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun YandexCaptchaWidget(
    siteKey: String,
    modifier: Modifier,
    onToken: (String) -> Unit
) {
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

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onToken(token: String) {
                        post { onToken(token) }
                    }
                }, "AndroidCallback")

                webViewClient = WebViewClient()

                loadDataWithBaseURL(
                    AUTH_URL,
                    yandexCaptchaHtml(siteKey),
                    MIME_TYPE,
                    ENCODING,
                    null
                )
            }
        },
        update = { webView ->
            webView.requestLayout()
        }
    )
}
