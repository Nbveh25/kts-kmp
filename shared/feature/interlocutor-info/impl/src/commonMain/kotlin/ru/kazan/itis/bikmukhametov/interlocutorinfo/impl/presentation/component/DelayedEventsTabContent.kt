package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserPlannedEventsUiState
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen

@Composable
internal fun DelayedEventsTabContent(
    userPlannedEventsState: UserPlannedEventsUiState,
    emptyEventsMessage: String,
    onRetryUserPlannedEvents: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            userPlannedEventsState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            userPlannedEventsState.error != null -> {
                ErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.paddingMedium),
                    errorMessage = userPlannedEventsState.error,
                    onRetry = onRetryUserPlannedEvents,
                )
            }

            userPlannedEventsState.items.isEmpty() -> {
                TabPlaceholderContent(
                    message = emptyEventsMessage,
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
                        count = userPlannedEventsState.items.size,
                        key = { index -> userPlannedEventsState.items[index].id },
                    ) { index ->
                        PlannedEventRow(event = userPlannedEventsState.items[index])
                    }
                }
            }
        }
    }
}

@Composable
private fun PlannedEventRow(
    event: InterlocutorPlannedEvent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = event.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        val subtitle = event.scheduledAt.takeIf { it.isNotBlank() } ?: "—"
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = Spacing.paddingSmall),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
