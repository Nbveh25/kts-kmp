package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserChatsUiState
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserListsUiState
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserPlannedEventsUiState
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserVarsUiState

@Immutable
internal data class InterlocutorInfoUiState(
    val userVars: UserVarsUiState = UserVarsUiState(),
    val userLists: UserListsUiState = UserListsUiState(),
    val userChats: UserChatsUiState = UserChatsUiState(),
    val userPlannedEvents: UserPlannedEventsUiState = UserPlannedEventsUiState(),
)
