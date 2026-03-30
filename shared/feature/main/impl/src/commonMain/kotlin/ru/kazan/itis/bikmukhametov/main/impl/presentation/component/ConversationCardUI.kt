package ru.kazan.itis.bikmukhametov.main.impl.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ConversationCardItem
import ru.kazan.itis.bikmukhametov.ui.util.formatTimeForUi
import ru.kazan.itis.bikmukhametov.ui.util.rememberTimeFormatStrings
import ru.kazan.itis.bikmukhametov.theme.CornerShape
import ru.kazan.itis.bikmukhametov.theme.Elevation
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ConvesationCardUI(
    chat: ConversationCardItem,
    modifier: Modifier = Modifier
) {
    val timeFormatStrings = rememberTimeFormatStrings()
    val timeOrDate = formatTimeForUi(chat.dateUpdatedIso, timeFormatStrings)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerShape.cornerShapeMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.cardShadowElevationMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                if (chat.avatarUrl != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        model = chat.avatarUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(18.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(chat.socialBadge),
                        contentDescription = "Иконка приложения",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Spacing.paddingMedium))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.paddingExtraSmall)
            ) {
                Text(
                    text = chat.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = chat.lastMessageText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 2
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Spacing.paddingExtraSmall)
            ) {
                Text(
                    text = timeOrDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
