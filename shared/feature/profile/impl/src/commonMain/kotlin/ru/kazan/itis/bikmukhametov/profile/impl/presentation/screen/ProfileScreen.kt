package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.kazan.itis.bikmukhametov.ui.components.AppBottomNav
import ru.kazan.itis.bikmukhametov.ui.components.CabinetProjectTopBar

@Composable
fun ProfileScreen(
    onChatsClick: () -> Unit,
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            CabinetProjectTopBar(
                currentCabinet = state.currentCabinet,
                cabinets = state.cabinets,
                cabinetDropdownExpanded = state.cabinetDropdownExpanded,
                onCabinetDropdownChange = viewModel::onSpaceDropdownChange,
                onCabinetSelect = {},
                currentProject = state.currentProject,
                projects = state.projects,
                projectDropdownExpanded = state.projectDropdownExpanded,
                onProjectDropdownChange = viewModel::onProjectDropdownChange,
                onProjectSelect = {},
            )
        },
        bottomBar = {
            AppBottomNav(
                chatsSelected = false,
                onChatsClick = onChatsClick,
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                ProfileErrorContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    errorMessage = state.error,
                    onRetry = { viewModel.onAction(ProfileAction.RetryLoad) }
                )
            }

            else -> {
                ProfileContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = state,
                    onToggleNotifications = { enabled ->
                        viewModel.onAction(ProfileAction.ToggleNotifications(enabled))
                    },
                    onLogout = { viewModel.onAction(ProfileAction.Logout) }
                )
            }
        }
    }
}
