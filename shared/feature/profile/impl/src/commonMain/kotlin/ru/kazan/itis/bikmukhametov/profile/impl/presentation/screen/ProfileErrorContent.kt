package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_button_error
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_error
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ProfileErrorContent(
    errorMessage: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage ?: stringResource(Res.string.profile_error),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(Spacing.paddingSmall))
        TextButton(onClick = onRetry) {
            Text(stringResource(Res.string.profile_button_error))
        }
    }
}
