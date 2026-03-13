package ru.kazan.itis.bikmukhametov.profile.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_notifications
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_push_notifications
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
fun ProfileNotificationsCard(
    notificationsEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.paddingSmall)
    ) {
        Text(
            text = stringResource(Res.string.profile_notifications),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.profile_push_notifications),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}
