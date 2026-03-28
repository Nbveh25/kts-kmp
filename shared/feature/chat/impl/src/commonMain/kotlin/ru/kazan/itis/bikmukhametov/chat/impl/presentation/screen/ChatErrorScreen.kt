package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

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
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_button_retry
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_error_message
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ChatErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Spacing.paddingMedium,
        vertical = Spacing.paddingSmall
    ),
    onRetry: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.paddingMedium)
        ) {
            Text(
                text = errorMessage ?: stringResource(Res.string.chat_error_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )

            Button(onClick = onRetry) {
                Text(text = stringResource(Res.string.chat_button_retry))
            }
        }
    }
}
