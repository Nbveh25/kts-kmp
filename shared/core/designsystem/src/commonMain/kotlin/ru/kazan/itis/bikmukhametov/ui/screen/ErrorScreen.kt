package ru.kazan.itis.bikmukhametov.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.Res
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_default_message
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_icon_content_description
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_retry
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.ic_error
import ru.kazan.itis.bikmukhametov.theme.Spacing

/**
 * Экран ошибки: иконка [ic_error], текст, кнопка «Повторить».
 *
 * @param errorMessage текст ошибки; если null — подставляется [error_default_message] из ресурсов.
 */
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    retryButtonText: String = stringResource(Res.string.error_retry),
    defaultErrorMessage: String = stringResource(Res.string.error_default_message),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Spacing.paddingMedium,
        vertical = Spacing.paddingSmall,
    ),
    verticalSpacing: Dp = Spacing.paddingMedium,
    iconSize: Dp = 144.dp,
    onRetry: () -> Unit = {},
    showRetryButton: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_error),
                contentDescription = stringResource(Res.string.error_icon_content_description),
                modifier = Modifier.size(iconSize),
                //tint = MaterialTheme.colorScheme.error,
            )
            Text(
                text = errorMessage ?: defaultErrorMessage,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (showRetryButton) {
                Button(onClick = onRetry) {
                    Text(text = retryButtonText)
                }
            }
        }
    }
}
