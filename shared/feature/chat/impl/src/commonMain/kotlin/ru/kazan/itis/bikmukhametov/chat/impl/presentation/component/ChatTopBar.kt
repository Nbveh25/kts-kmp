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
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_arrow_back_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_more_vert_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_pause_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_play_arrow_24
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ChatTopBar(
    interlocutorName: String,
    interlocutorAvatarUrl: String?,
    onBack: () -> Unit,
    onUserInfoClick: () -> Unit,
    botRunning: Boolean,
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
                        contentDescription = "Назад"
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
                                .size(40.dp)
                                .clip(CircleShape),
                            model = interlocutorAvatarUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
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
                modifier = Modifier
                    .background(
                        if (botRunning) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (botRunning) vectorResource(Res.drawable.ic_play_arrow_24) else vectorResource(Res.drawable.ic_pause_24),
                    tint = if (botRunning) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error, //if (botRunning)
                    contentDescription = "Play"
                )
            }

            Box {
                IconButton(onClick = { onMenuExpandChange(!menuExpanded) }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_more_vert_24),
                        contentDescription = "Ещё"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { onMenuExpandChange(false) },
                ) {
                    DropdownMenuItem(
                        text = { Text("Запуск сценария (выбор блока)") },
                        onClick = onRunScenario,
                    )
                }
            }
        }
    }
}
