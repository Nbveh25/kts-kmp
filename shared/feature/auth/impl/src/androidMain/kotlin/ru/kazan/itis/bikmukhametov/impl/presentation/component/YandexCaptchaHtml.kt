package ru.kazan.itis.bikmukhametov.impl.presentation.component

/**
 * HTML-страница для виджета Yandex Smart Captcha в WebView.
 * [siteKey] подставляется в data-sitekey контейнера.
 */
internal fun yandexCaptchaHtml(siteKey: String): String = """
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
