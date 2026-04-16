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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_label_channel
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_label_chat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.mapper.drawableResourceForChannelKind
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserVarsUiState
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen

@Composable
internal fun VariablesTabContent(
    channelKind: ChannelKind,
    selectedChannel: String,
    selectedChat: String,
    options: List<String>,
    onChannelSelected: (String) -> Unit,
    onChatSelected: (String) -> Unit,
    userVarsState: UserVarsUiState,
    emptyVariablesMessage: String,
    onRetryUserVars: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.paddingMedium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.paddingMedium),
        ) {
            ChannelOrChatDropdown(
                label = stringResource(Res.string.interlocutor_info_label_channel),
                selected = selectedChannel,
                options = options,
                onSelected = onChannelSelected,
                leadingInField = {
                    Image(
                        painter = painterResource(drawableResourceForChannelKind(channelKind)),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                    )
                },
                modifier = Modifier.weight(1f),
            )
            ChannelOrChatDropdown(
                label = stringResource(Res.string.interlocutor_info_label_chat),
                selected = selectedChat,
                options = options,
                onSelected = onChatSelected,
                leadingInField = null,
                modifier = Modifier.weight(1f),
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            when {
                userVarsState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                userVarsState.error != null -> {
                    ErrorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.paddingMedium),
                        errorMessage = userVarsState.error,
                        onRetry = onRetryUserVars,
                    )
                }

                userVarsState.items.isEmpty() -> {
                    TabPlaceholderContent(
                        message = emptyVariablesMessage,
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
                            count = userVarsState.items.size,
                            key = { index -> "user_var_$index" },
                        ) { index ->
                            VariableRow(field = userVarsState.items[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VariableRow(
    field: InterlocutorCustomField,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = field.key.ifBlank { "—" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f),
        )
        Text(
            text = field.value.ifBlank { "—" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.55f),
        )
    }
}
