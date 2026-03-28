package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_back_desc
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_the_script
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_start_bot_desc
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_state_bot_desc
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_stop_bot_desc
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_arrow_back_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_more_vert_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_pause_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_play_arrow_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.more_desc
import ru.kazan.itis.bikmukhametov.theme.Dimensions
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ChatTopBar(
    interlocutorName: String,
    interlocutorAvatarUrl: String?,
    onBack: () -> Unit,
    onUserInfoClick: () -> Unit,
    botRunning: Boolean?,
    onBotToggle: () -> Unit,
    menuExpanded: Boolean,
    onMenuExpandChange: (Boolean) -> Unit,
    onRunScenario: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.paddingSmall, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_arrow_back_24),
                        contentDescription = stringResource(Res.string.chat_back_desc)
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onUserInfoClick() }
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (interlocutorAvatarUrl != null) {
                        AsyncImage(
                            modifier = Modifier
                                .size(Dimensions.chatTopBarAvatarSize)
                                .clip(CircleShape),
                            model = interlocutorAvatarUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(Dimensions.chatTopBarAvatarSize)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = interlocutorName.take(1).uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(Spacing.paddingSmall))
                    Text(
                        text = interlocutorName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                    )
                }
            }

            IconButton(
                onClick = onBotToggle,
                enabled = botRunning != null,
                modifier = Modifier
                    .background(
                        when (botRunning) {
                            true -> MaterialTheme.colorScheme.errorContainer
                            false -> MaterialTheme.colorScheme.primaryContainer
                            null -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = when (botRunning) {
                        true -> vectorResource(Res.drawable.ic_pause_24)
                        false -> vectorResource(Res.drawable.ic_play_arrow_24)
                        null -> vectorResource(Res.drawable.ic_play_arrow_24)
                    },
                    tint = when (botRunning) {
                        true -> MaterialTheme.colorScheme.error
                        false -> MaterialTheme.colorScheme.onPrimaryContainer
                        null -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    contentDescription = when (botRunning) {
                        true -> stringResource(Res.string.chat_stop_bot_desc)
                        false -> stringResource(Res.string.chat_start_bot_desc)
                        null -> stringResource(Res.string.chat_state_bot_desc)
                    }
                )
            }

            Box {
                IconButton(onClick = { onMenuExpandChange(!menuExpanded) }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_more_vert_24),
                        contentDescription = stringResource(Res.string.more_desc)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { onMenuExpandChange(false) },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.chat_run_the_script)) },
                        onClick = onRunScenario,
                    )
                }
            }
        }
    }
}
