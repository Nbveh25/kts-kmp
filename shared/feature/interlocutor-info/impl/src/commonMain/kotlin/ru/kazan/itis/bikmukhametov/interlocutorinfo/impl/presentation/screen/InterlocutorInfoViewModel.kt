package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserChatsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserListsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserPlannedEventsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserVarsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserChatsUiState
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserListsUiState
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserPlannedEventsUiState
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state.UserVarsUiState
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class InterlocutorInfoViewModel(
    private val chatId: String,
    private val userId: String,
    private val getUserVarsUseCase: GetUserVarsUseCase,
    private val getUserListsUseCase: GetUserListsUseCase,
    private val getUserChatsUseCase: GetUserChatsUseCase,
    private val getUserPlannedEventsUseCase: GetUserPlannedEventsUseCase,
) : BaseViewModel<InterlocutorInfoUiState, InterlocutorInfoAction>(InterlocutorInfoUiState()) {

    init {
        onAction(InterlocutorInfoAction.RefreshUserVars)
    }

    override fun onAction(action: InterlocutorInfoAction) {
        when (action) {
            InterlocutorInfoAction.RefreshUserVars -> refreshUserVars()
            InterlocutorInfoAction.RefreshUserLists -> refreshUserLists()
            InterlocutorInfoAction.RefreshUserChats -> refreshUserChats()
            InterlocutorInfoAction.RefreshUserPlannedEvents -> refreshUserPlannedEvents()
        }
    }

    private fun refreshUserVars() {
        if (chatId.isBlank() || userId.isBlank()) {
            updateState {
                copy(userVars = UserVarsUiState())
            }
            return
        }
        viewModelScope.launch {
            updateState {
                copy(userVars = userVars.copy(isLoading = true, error = null))
            }
            getUserVarsUseCase(chatId, userId).fold(
                onSuccess = { list ->
                    updateState {
                        copy(
                            userVars = userVars.copy(
                                items = list,
                                isLoading = false,
                                error = null,
                            ),
                        )
                    }
                },
                onFailure = { e ->
                    updateState {
                        copy(
                            userVars = userVars.copy(
                                isLoading = false,
                                error = e.message,
                            ),
                        )
                    }
                },
            )
        }
    }

    private fun refreshUserLists() {
        if (userId.isBlank()) {
            updateState {
                copy(userLists = UserListsUiState())
            }
            return
        }
        viewModelScope.launch {
            updateState {
                copy(userLists = userLists.copy(isLoading = true, error = null))
            }
            getUserListsUseCase(userId).fold(
                onSuccess = { list ->
                    updateState {
                        copy(
                            userLists = userLists.copy(
                                items = list,
                                isLoading = false,
                                error = null,
                            ),
                        )
                    }
                },
                onFailure = { e ->
                    updateState {
                        copy(
                            userLists = userLists.copy(
                                isLoading = false,
                                error = e.message,
                            ),
                        )
                    }
                },
            )
        }
    }

    private fun refreshUserChats() {
        if (userId.isBlank()) {
            updateState {
                copy(userChats = UserChatsUiState())
            }
            return
        }
        viewModelScope.launch {
            updateState {
                copy(userChats = userChats.copy(isLoading = true, error = null))
            }
            getUserChatsUseCase(userId).fold(
                onSuccess = { list ->
                    updateState {
                        copy(
                            userChats = userChats.copy(
                                items = list,
                                isLoading = false,
                                error = null,
                            ),
                        )
                    }
                },
                onFailure = { e ->
                    updateState {
                        copy(
                            userChats = userChats.copy(
                                isLoading = false,
                                error = e.message,
                            ),
                        )
                    }
                },
            )
        }
    }

    private fun refreshUserPlannedEvents() {
        if (chatId.isBlank() || userId.isBlank()) {
            updateState {
                copy(userPlannedEvents = UserPlannedEventsUiState())
            }
            return
        }
        viewModelScope.launch {
            updateState {
                copy(userPlannedEvents = userPlannedEvents.copy(isLoading = true, error = null))
            }
            getUserPlannedEventsUseCase(chatId, userId).fold(
                onSuccess = { list ->
                    updateState {
                        copy(
                            userPlannedEvents = userPlannedEvents.copy(
                                items = list,
                                isLoading = false,
                                error = null,
                            ),
                        )
                    }
                },
                onFailure = { e ->
                    updateState {
                        copy(
                            userPlannedEvents = userPlannedEvents.copy(
                                isLoading = false,
                                error = e.message,
                            ),
                        )
                    }
                },
            )
        }
    }
}
