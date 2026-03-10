package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import ru.kazan.itis.bikmukhametov.theme.Spacing

/**
 * Экран отображения ошибки с возможностью повтора
 *
 * @param modifier модификатор для внешнего контейнера
 * @param errorMessage текст ошибки (если null — используется дефолтное сообщение)
 * @param retryButtonText текст кнопки повтора
 * @param contentPadding отступы вокруг контента
 * @param onRetry лямбда для обработки повторной попытки загрузки
 * @param showRetryButton показывать ли кнопку повтора (по умолчанию true)
 */
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    retryButtonText: String = "Повторить",
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Spacing.paddingMedium,
        vertical = Spacing.paddingSmall
    ),
    verticalSpacing: Dp = Spacing.paddingMedium,
    onRetry: () -> Unit = {},
    showRetryButton: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(verticalSpacing)
        ) {
            Text(
                text = errorMessage ?: "Произошла ошибка",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )

            if (showRetryButton) {
                Button(onClick = onRetry) {
                    Text(text = retryButtonText)
                }
            }
        }
    }
}