package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.profile.api.usecase.GetProfileInfoUseCase
import ru.kazan.itis.bikmukhametov.profile.api.usecase.LogoutUseCase
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.model.toItem
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class ProfileViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCabinetUseCase: GetCabinetUseCase,
    private val getProjectListUseCase: GetProjectListUseCase,
) : BaseViewModel<ProfileUiState, ProfileAction>(ProfileUiState()) {

    init {
        loadProfile()
        loadCabinetAndProjects()
    }

    private fun loadCabinetAndProjects() {
        viewModelScope.launch {
            getCabinetUseCase().onSuccess { cabinetModels ->
                val cabinetsUi = cabinetModels.map { it.toItem() }
                updateState {
                    copy(
                        currentCabinet = cabinetsUi.firstOrNull(),
                        cabinets = cabinetsUi,
                    )
                }
            }
            getProjectListUseCase().onSuccess { projects ->
                val projectsUi = projects.map { it.toItem() }
                updateState {
                    copy(
                        currentProject = projectsUi.firstOrNull(),
                        projects = projectsUi,
                    )
                }
            }
        }
    }

    override fun onAction(action: ProfileAction) {
        when (action) {

            is ProfileAction.Logout -> {
                logout()
            }

            is ProfileAction.RetryLoad -> {
                loadProfile()
            }

            is ProfileAction.ToggleNotifications -> updateState {
                copy(notificationsEnabled = action.enabled)
            }

            is ProfileAction.ToggleCabinetDropdown -> updateState {
                copy(cabinetDropdownExpanded = action.expanded)
            }

            is ProfileAction.ToggleProjectDropdown -> updateState {
                copy(projectDropdownExpanded = action.expanded)
            }

            is ProfileAction.SelectCabinet -> updateState {
                copy(currentCabinet = action.cabinet, cabinetDropdownExpanded = false)
            }

            is ProfileAction.SelectProject -> updateState {
                copy(currentProject = action.project, projectDropdownExpanded = false)
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
}
