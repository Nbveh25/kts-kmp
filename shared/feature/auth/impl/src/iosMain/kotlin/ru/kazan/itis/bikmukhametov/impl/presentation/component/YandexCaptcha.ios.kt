package ru.kazan.itis.bikmukhametov.impl.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun YandexCaptchaWidget(
    siteKey: String,
    modifier: Modifier,
    onToken: (String) -> Unit
) {
    // заглушка для iOS
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Yandex Captcha (iOS stub)")
    }
}