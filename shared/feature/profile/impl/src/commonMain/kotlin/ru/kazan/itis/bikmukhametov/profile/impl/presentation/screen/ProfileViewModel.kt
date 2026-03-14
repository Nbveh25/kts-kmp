package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.profile.api.usecase.GetProfileInfoUseCase
import ru.kazan.itis.bikmukhametov.profile.api.usecase.LogoutUseCase
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.model.toItem
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class ProfileViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<ProfileUiState, ProfileAction>(ProfileUiState()) {

    init {
        loadProfile()
    }

    override fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.ToggleNotifications -> updateState {
                copy(notificationsEnabled = action.enabled)
            }

            ProfileAction.Logout -> {
                logout()
            }

            ProfileAction.RetryLoad -> {
                loadProfile()
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getProfileInfoUseCase()
                .onSuccess { model ->
                    updateState { copy(isLoading = false, profile = model.toItem()) }
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            updateState { copy(isLoggingOut = true) }
            logoutUseCase()
        }
    }

    fun onSpaceDropdownChange(expanded: Boolean) {
        updateState { copy(cabinetDropdownExpanded = expanded) }
    }

    fun onProjectDropdownChange(expanded: Boolean) {
        updateState { copy(projectDropdownExpanded = expanded) }
    }
}
