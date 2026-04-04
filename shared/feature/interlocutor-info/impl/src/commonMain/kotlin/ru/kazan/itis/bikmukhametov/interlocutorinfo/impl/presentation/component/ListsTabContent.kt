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
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserListsUiState
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen

@Composable
internal fun ListsTabContent(
    userListsState: UserListsUiState,
    emptyListsMessage: String,
    onRetryUserLists: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            userListsState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            userListsState.error != null -> {
                ErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.paddingMedium),
                    errorMessage = userListsState.error,
                    onRetry = onRetryUserLists,
                )
            }

            userListsState.items.isEmpty() -> {
                TabPlaceholderContent(
                    message = emptyListsMessage,
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
                        count = userListsState.items.size,
                        key = { index -> userListsState.items[index].id },
                    ) { index ->
                        UserListRow(list = userListsState.items[index])
                    }
                }
            }
        }
    }
}

@Composable
private fun UserListRow(
    list: InterlocutorUserList,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = list.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (list.type.isNotBlank()) {
            Text(
                text = list.type,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = Spacing.paddingSmall),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
