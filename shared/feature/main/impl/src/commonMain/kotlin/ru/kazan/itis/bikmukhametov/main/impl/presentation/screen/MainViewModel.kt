package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toConversationCardItem
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toUi
import kotlin.collections.emptyList

internal class MainViewModel(
    private val getCabinetUseCase: GetCabinetUseCase,
    private val getProjectListUseCase: GetProjectListUseCase,
    private val getConversationListUseCase: GetConversationListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    init {
        // экран - загрузка
        viewModelScope.launch {

            val cabinetDeferred = async { getCabinetUseCase() }
            val projectListDeferred = async { getProjectListUseCase() }
            val conversationListDeferred = async { getConversationListUseCase() }

            val cabinetResult = cabinetDeferred.await()
            val projectListResult = projectListDeferred.await()
            val conversationListResult = conversationListDeferred.await()

            cabinetResult.onSuccess { cabinetModel ->
                val cabinetUi = cabinetModel.toUi()
                updateState {
                    copy(
                        currentCabinet = cabinetUi,
                        cabinets = listOf(cabinetUi)
                    )
                }
            }.onFailure {
                // экран ошибка
            }

            projectListResult.onSuccess { projectModels ->
                val projectsListUi = projectModels.map { it.toUi() }
                updateState {
                    copy(
                        currentProject = projectsListUi.first(), // подумать о null
                        projects = projectsListUi
                    )
                }
            }.onFailure {
                // экран ошибка
            }

            conversationListResult.onSuccess { conversationModels ->
                val conversatioCardListUI = conversationModels.map { it.toConversationCardItem() }
                updateState {
                    copy(
                        chats = conversatioCardListUI
                    )
                }
            }.onFailure {
                // экран ошибка - перезагрузить
            }
            
        }
    }

    private fun createInitialState(): MainUiState {
        return MainUiState(
            currentCabinet = null,
            cabinets = emptyList(),
            currentProject = null,
            projects = emptyList(),
            chats = emptyList()
        )
    }

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.ToggleCabinetDropdown -> updateState {
                copy(cabinetDropdownExpanded = !cabinetDropdownExpanded)
            }

            is MainAction.SelectCabinet -> updateState {
                copy(currentCabinet = action.cabinet, cabinetDropdownExpanded = false)
            }

            MainAction.ToggleProjectDropdown -> updateState {
                copy(projectDropdownExpanded = !projectDropdownExpanded)
            }

            is MainAction.SelectProject -> updateState {
                copy(currentProject = action.project, projectDropdownExpanded = false)
            }

            MainAction.ToggleSearch -> updateState {
                copy(searchExpanded = !searchExpanded)
            }

            is MainAction.SearchQueryChanged -> updateState {
                copy(searchQuery = action.query)
            }

            MainAction.ToggleFilterSheet -> updateState {
                copy(filterSheetVisible = !filterSheetVisible)
            }

            is MainAction.SelectTab -> updateState {
                copy(selectedTab = action.tab)
            }
        }
    }

    fun onSpaceDropdownChange(expanded: Boolean) {
        updateState { copy(cabinetDropdownExpanded = expanded) }
    }

    fun onProjectDropdownChange(expanded: Boolean) {
        updateState { copy(projectDropdownExpanded = expanded) }
    }

    fun onDismissFilterSheet() {
        updateState { copy(filterSheetVisible = false) }
    }

    private fun updateState(block: MainUiState.() -> MainUiState) {
        _state.value = _state.value.block()
    }
}
