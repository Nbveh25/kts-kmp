package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.ChatCard
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.ChatListTabs
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.ChatListTopBar
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.FilterBottomSheet
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.MainBottomNav
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        ChatListTopBar(
            currentSpace = state.currentSpace,
            spaces = state.spaces,
            spaceDropdownExpanded = state.spaceDropdownExpanded,
            onSpaceDropdownChange = viewModel::onSpaceDropdownChange,
            onSpaceSelect = { viewModel.onAction(MainAction.SelectSpace(it)) },
            searchExpanded = state.searchExpanded,
            searchQuery = state.searchQuery,
            onSearchQueryChange = { viewModel.onAction(MainAction.SearchQueryChanged(it)) },
            onSearchToggle = { viewModel.onAction(MainAction.ToggleSearch) },
            onFilterClick = { viewModel.onAction(MainAction.ToggleFilterSheet) }
        )

        ChatListTabs(
            selectedTab = state.selectedTab,
            onTabSelect = { viewModel.onAction(MainAction.SelectTab(it)) },
            modifier = Modifier.padding(horizontal = Spacing.paddingMedium, vertical = Spacing.paddingSmall)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = state.chats,
                key = { it.id }
            ) { chat ->
                ChatCard(
                    chat = chat,
                    modifier = Modifier.padding(
                        horizontal = Spacing.paddingMedium,
                        vertical = Spacing.paddingExtraSmall
                    )
                )
            }
        }

        MainBottomNav(
            chatsSelected = true,
            onChatsClick = { },
            onProfileClick = { }
        )
    }

    if (state.filterSheetVisible) {
        FilterBottomSheet(onDismiss = viewModel::onDismissFilterSheet)
    }
}
