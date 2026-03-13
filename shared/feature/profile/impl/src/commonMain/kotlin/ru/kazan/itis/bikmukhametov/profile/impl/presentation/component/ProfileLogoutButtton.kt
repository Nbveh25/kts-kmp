package ru.kazan.itis.bikmukhametov.profile.impl.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_logout

@Composable
fun ProfileLogoutButton(
    isLoggingOut: Boolean,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onLogout,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = !isLoggingOut,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        if (isLoggingOut) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        } else {
            Text(
                text = stringResource(Res.string.profile_logout),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
