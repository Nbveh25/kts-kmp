package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toConversationCardItem
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toUi

internal class MainViewModel(
    private val getCabinetUseCase: GetCabinetUseCase,
    private val getProjectListUseCase: GetProjectListUseCase,
    private val getConversationListUseCase: GetConversationListUseCase
) : ViewModel() {

    private var currentOffset = 0
    private var isPageLoading = false
    private var isEndReached = false

    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    init {
        loadDataSequentially()
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.ToggleCabinetDropdown -> updateState {
                copy(cabinetDropdownExpanded = !cabinetDropdownExpanded)
            }

            is MainAction.SelectCabinet -> updateState {
                copy(currentCabinet = action.cabinet, cabinetDropdownExpanded = false)
            }

            is MainAction.ToggleProjectDropdown -> updateState {
                copy(projectDropdownExpanded = !projectDropdownExpanded)
            }

            is MainAction.SelectProject -> updateState {
                copy(currentProject = action.project, projectDropdownExpanded = false)
            }

            is MainAction.ToggleSearch -> updateState {
                copy(
                    searchExpanded = !searchExpanded,
                    searchQuery = if (searchExpanded) "" else searchQuery
                ).recomputed()
            }

            is MainAction.SearchQueryChanged -> updateState {
                copy(searchQuery = action.query).recomputed()
            }

            is MainAction.ToggleFilterSheet -> updateState {
                copy(filterSheetVisible = !filterSheetVisible)
            }

            is MainAction.SelectTab -> updateState {
                copy(selectedTab = action.tab).recomputed()
            }

            is  MainAction.Refresh -> refreshConversations()
        }
    }

    fun onListEndReached() {
        if (isEndReached || isPageLoading) return
        viewModelScope.launch { loadConversations() }
    }

    fun onCabinetDropdownChange(expanded: Boolean) {
        updateState { copy(cabinetDropdownExpanded = expanded) }
    }

    fun onProjectDropdownChange(expanded: Boolean) {
        updateState { copy(projectDropdownExpanded = expanded) }
    }

    fun onDismissFilterSheet() {
        updateState { copy(filterSheetVisible = false) }
    }

    fun onRetryClick() {
        currentOffset = 0
        isEndReached = false
        loadDataSequentially()
    }

    private fun refreshConversations() {
        viewModelScope.launch {
            updateState { copy(isRefreshing = true, loadError = null) }
            currentOffset = 0
            isEndReached = false
            isPageLoading = false
            loadConversations(reset = true)
            updateState { copy(isRefreshing = false) }
        }
    }

    // Вычисляет chats из allChats с учётом выбранной вкладки и поискового запроса.
    // Вызывается на receiver'е состояния, чтобы одним copy() обновить и allChats и chats.
    private fun MainUiState.recomputed(): MainUiState {
        val filtered = allChats
            .filter { chat ->
                when (selectedTab) {
                    ChatListTab.ALL -> true
                    ChatListTab.WAITING -> chat.isWaiting
                }
            }
            .filter { chat ->
                searchQuery.isBlank() ||
                        chat.name.contains(searchQuery, ignoreCase = true) ||
                        chat.lastMessageText.contains(searchQuery, ignoreCase = true)
            }
        return copy(chats = filtered)
    }

    private fun loadDataSequentially() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, loadError = null) }
            if (!loadCabinet()) return@launch
            if (!loadProjects()) return@launch
            currentOffset = 0
            isEndReached = false
            loadConversations(reset = true)
        }
    }

    private suspend fun loadCabinet(): Boolean {
        return getCabinetUseCase().fold(
            onSuccess = { cabinetModel ->
                val cabinetUi = cabinetModel.toUi()
                updateState {
                    copy(
                        currentCabinet = cabinetUi,
                        cabinets = listOf(cabinetUi)
                    )
                }
                true
            },
            onFailure = { error ->
                updateState {
                    copy(
                        isLoading = false,
                        loadError = error.message ?: "Ошибка загрузки кабинета"
                    )
                }
                false
            }
        )
    }

    private suspend fun loadProjects(): Boolean {
        return getProjectListUseCase().fold(
            onSuccess = { projectModels ->
                val projectsListUi = projectModels.map { it.toUi() }
                updateState {
                    copy(
                        currentProject = projectsListUi.firstOrNull(),
                        projects = projectsListUi
                    )
                }
                true
            },
            onFailure = { error ->
                updateState {
                    copy(
                        isLoading = false,
                        loadError = error.message ?: "Ошибка загрузки проектов"
                    )
                }
                false
            }
        )
    }

    private suspend fun loadConversations(reset: Boolean = false) {
        if (isPageLoading) return

        val offsetToLoad = if (reset) 0 else currentOffset

        isPageLoading = true
        updateState {
            if (offsetToLoad == 0) copy(isLoading = true, loadError = null)
            else copy(isLoadingMore = true, loadError = null)
        }

        getConversationListUseCase(limit = PAGE_SIZE, offset = offsetToLoad)
            .onSuccess { conversationModels ->
                val newItems = conversationModels.map { it.toConversationCardItem() }

                if (newItems.size < PAGE_SIZE) isEndReached = true
                currentOffset = offsetToLoad + newItems.size

                updateState {
                    val updatedAll = if (offsetToLoad == 0) newItems else allChats + newItems
                    copy(
                        allChats = updatedAll,
                        isLoading = false,
                        isLoadingMore = false,
                        loadError = null
                    ).recomputed()
                }
            }
            .onFailure { error ->
                updateState {
                    copy(
                        isLoading = false,
                        isLoadingMore = false,
                        loadError = error.message
                    )
                }
            }

        isPageLoading = false
    }

    private fun updateState(block: MainUiState.() -> MainUiState) {
        _state.value = _state.value.block()
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
