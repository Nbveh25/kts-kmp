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
import androidx.compose.runtime.remember
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
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_user_json_schema
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_user_network
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_user_ssl
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_user_timeout
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.error_user_unauthorized
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.ic_error
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.error.UserFacingErrorKind
import ru.kazan.itis.bikmukhametov.ui.error.classifyUserError
import ru.kazan.itis.bikmukhametov.ui.error.truncateForUser

/**
 * Экран ошибки: иконка [ic_error], текст, кнопка «Повторить».
 *
 * @param errorMessage сырое сообщение (например, с API); на экране показывается в человекочитаемом виде.
 */
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    /** Если false — показывается исходный [errorMessage] без маппинга (кроме пустого/null). */
    mapToUserFacingText: Boolean = true,
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
    val kind = remember(errorMessage, mapToUserFacingText) {
        if (mapToUserFacingText) classifyUserError(errorMessage) else UserFacingErrorKind.ShowAsIs
    }
    val resolvedMessage = when {
        errorMessage.isNullOrBlank() -> defaultErrorMessage
        !mapToUserFacingText -> errorMessage
        else -> when (kind) {
            UserFacingErrorKind.UseDefault -> defaultErrorMessage
            UserFacingErrorKind.JsonSchemaMismatch -> stringResource(Res.string.error_user_json_schema)
            UserFacingErrorKind.Network -> stringResource(Res.string.error_user_network)
            UserFacingErrorKind.Timeout -> stringResource(Res.string.error_user_timeout)
            UserFacingErrorKind.Ssl -> stringResource(Res.string.error_user_ssl)
            UserFacingErrorKind.Unauthorized -> stringResource(Res.string.error_user_unauthorized)
            UserFacingErrorKind.ShowAsIs -> truncateForUser(errorMessage)
        }
    }

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
                text = resolvedMessage,
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
