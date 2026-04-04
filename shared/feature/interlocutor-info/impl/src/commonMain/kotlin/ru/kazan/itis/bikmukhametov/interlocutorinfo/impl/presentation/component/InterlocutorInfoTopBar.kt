package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_close_24
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_close
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun InterlocutorInfoTopBar(
    interlocutorName: String,
    channelKind: ChannelKind,
    onClose: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.paddingMedium,
                    end = Spacing.paddingSmall,
                    top = Spacing.paddingSmall,
                    bottom = Spacing.paddingSmall,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.paddingSmall),
            ) {
                ChannelKindBadgeIcon(channelKind = channelKind)
                Text(
                    text = interlocutorName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_close_24),
                    contentDescription = stringResource(Res.string.interlocutor_info_close),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
