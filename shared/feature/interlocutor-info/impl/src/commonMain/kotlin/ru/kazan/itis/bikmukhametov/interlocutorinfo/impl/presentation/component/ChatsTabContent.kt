package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.mapper.channelKindFromApiRaw
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.mapper.drawableResourceForChannelKind
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserChatsUiState
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen

@Composable
internal fun ChatsTabContent(
    userChatsState: UserChatsUiState,
    emptyChatsMessage: String,
    onRetryUserChats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            userChatsState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            userChatsState.error != null -> {
                ErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.paddingMedium),
                    errorMessage = userChatsState.error,
                    onRetry = onRetryUserChats,
                )
            }

            userChatsState.items.isEmpty() -> {
                TabPlaceholderContent(
                    message = emptyChatsMessage,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Spacing.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.paddingSmall),
                ) {
                    items(
                        count = userChatsState.items.size,
                        key = { index -> userChatsState.items[index].id },
                    ) { index ->
                        UserChatRow(chat = userChatsState.items[index])
                    }
                }
            }
        }
    }
}

@Composable
private fun UserChatRow(
    chat: InterlocutorUserChat,
    modifier: Modifier = Modifier,
) {
    val kind = channelKindFromApiRaw(chat.channelKind)
    val subtitle = buildList {
        //if (chat.externalId.isNotBlank()) add(chat.externalId)
        if (chat.realm.isNotBlank()) add(chat.realm)
        //if (chat.channelKind.isNotBlank()) add(chat.channelKind)
    }.joinToString(" · ").ifBlank { "—" }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (chat.isEnabled) 1f else 0.55f),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(drawableResourceForChannelKind(kind)),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Spacing.paddingMedium),
            ) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = Spacing.paddingSmall),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
