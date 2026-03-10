package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeSearchQuery()
        loadDataSequentially()
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

            MainAction.ToggleSearch -> {
                updateState {
                    copy(
                        searchExpanded = !searchExpanded,
                        searchQuery = if (searchExpanded) "" else searchQuery
                    )
                }
                searchQueryFlow.value = ""
            }

            is MainAction.SearchQueryChanged -> {
                updateState {
                    copy(searchQuery = action.query)
                }
                searchQueryFlow.value = action.query
            }

            MainAction.ToggleFilterSheet -> updateState {
                copy(filterSheetVisible = !filterSheetVisible)
            }

            is MainAction.SelectTab -> updateState {
                copy(selectedTab = action.tab)
            }
        }
    }

    private fun observeSearchQuery() {
        searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                flow {
                    val offsetToLoad = 0

                    isPageLoading = true
                    isEndReached = false
                    currentOffset = 0

                    updateState {
                        copy(
                            isLoading = true,
                            isLoadingMore = false,
                            loadError = null
                        )
                    }

                    val conversationListResult = getConversationListUseCase(
                        limit = PAGE_SIZE,
                        offset = offsetToLoad
                    )

                    val mappedResult = conversationListResult.map { conversationModels ->
                        val items = conversationModels.map { it.toConversationCardItem() }
                        if (query.isBlank()) {
                            items
                        } else {
                            items.filter { item ->
                                item.name.contains(query, ignoreCase = true) ||
                                        item.lastMessageText.contains(query, ignoreCase = true)
                            }
                        }
                    }

                    emit(mappedResult)
                }
            }
            .onEach { result ->
                result
                    .onSuccess { items ->
                        isPageLoading = false
                        isEndReached = items.size < PAGE_SIZE
                        currentOffset = items.size

                        updateState {
                            copy(
                                chats = items,
                                isLoading = false,
                                isLoadingMore = false,
                                loadError = null
                            )
                        }
                    }
                    .onFailure { error ->
                        isPageLoading = false
                        updateState {
                            copy(
                                isLoading = false,
                                isLoadingMore = false,
                                loadError = error.message
                            )
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

    fun onListEndReached() {
        if (isEndReached || isPageLoading) return

        viewModelScope.launch {
            loadConversations()
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

    fun onRetryClick() {
        currentOffset = 0
        isEndReached = false
        loadDataSequentially()
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
                true // успех
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
            if (offsetToLoad == 0) {
                copy(isLoading = true, loadError = null)
            } else {
                copy(isLoadingMore = true, loadError = null)
            }
        }

        val conversationListResult = getConversationListUseCase(
            limit = PAGE_SIZE,
            offset = offsetToLoad
        )

        conversationListResult
            .onSuccess { conversationModels ->
                val newItems = conversationModels.map { it.toConversationCardItem() }

                if (newItems.size < PAGE_SIZE) {
                    isEndReached = true
                }

                currentOffset = offsetToLoad + newItems.size

                updateState {
                    copy(
                        chats = if (offsetToLoad == 0) {
                            newItems
                        } else {
                            chats + newItems
                        },
                        isLoading = false,
                        isLoadingMore = false,
                        loadError = null
                    )
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
        private const val SEARCH_DEBOUNCE_MS = 400L
        private const val PAGE_SIZE = 20
    }
}
