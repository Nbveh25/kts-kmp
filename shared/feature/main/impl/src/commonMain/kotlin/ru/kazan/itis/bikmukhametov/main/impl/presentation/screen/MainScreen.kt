package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.chat.ChatListTopBar
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.filter.FilterBottomSheet
import ru.kazan.itis.bikmukhametov.ui.component.AppBottomNav
import ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen

@Composable
fun MainScreen(
    onProfileClick: () -> Unit,
    onChatClick: (conversationId: String) -> Unit = {},
) {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            ChatListTopBar(
                currentCabinet = state.currentCabinet,
                cabinets = state.cabinets,
                cabinetDropdownExpanded = state.cabinetDropdownExpanded,
                onCabinetDropdownChange = { viewModel.onAction(MainAction.CabinetDropdownChange(it)) },
                onCabinetSelect = { viewModel.onAction(MainAction.SelectCabinet(it)) },

                currentProject = state.currentProject,
                projects = state.projects,
                projectDropdownExpanded = state.projectDropdownExpanded,
                onProjectDropdownChange = { viewModel.onAction(MainAction.ProjectDropdownChange(it)) },
                onProjectSelect = { viewModel.onAction(MainAction.SelectProject(it)) },

                searchExpanded = state.searchExpanded,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { viewModel.onAction(MainAction.SearchQueryChanged(it)) },
                onSearchToggle = { viewModel.onAction(MainAction.ToggleSearch) },
                onFilterClick = { viewModel.onAction(MainAction.ToggleFilterSheet) }
            )
        },
        bottomBar = {
            AppBottomNav(
                chatsSelected = true,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->
        when {

            // Загрузка
            state.isLoading && state.chats.isEmpty() && state.loadError == null -> {
                ShimmerScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            // Ошибка
            state.loadError != null && state.chats.isEmpty() -> {
                ErrorScreen(
                    modifier = Modifier.padding(paddingValues),
                    errorMessage = state.loadError,
                    onRetry = { viewModel.onAction(MainAction.RetryClick) }
                )
            }

            // Успех
            else -> {
                ContentScreen(
                    modifier = Modifier.padding(paddingValues),
                    selectedTab = state.selectedTab,
                    onTabSelect = { viewModel.onAction(MainAction.SelectTab(it)) },
                    chats = state.chats,
                    isLoadingMore = state.isLoadingMore,
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.onAction(MainAction.Refresh) },
                    onListEndReached = { viewModel.onAction(MainAction.ListEndReached) },
                    onChatClick = { chat -> onChatClick(chat.id) }
                )
            }
            
        }
    }

    if (state.filterSheetVisible) {
        FilterBottomSheet(
            allChats = state.allChats,
            userListOption = state.userListOption,
            draftKinds = state.filterDraftKinds,
            draftChannelIds = state.filterDraftChannelIds,
            draftBuckets = state.filterDraftBuckets,
            onDraftKindsChange = { viewModel.onAction(MainAction.FilterDraftKindsChange(it)) },
            onDraftChannelsChange = { viewModel.onAction(MainAction.FilterDraftChannelsChange(it)) },
            onDraftBucketsChange = { viewModel.onAction(MainAction.FilterDraftBucketsChange(it)) },
            onApply = { viewModel.onAction(MainAction.ApplyChatFilters) },
            onDismiss = { viewModel.onAction(MainAction.DismissFilterSheet) },
        )
    }
}
