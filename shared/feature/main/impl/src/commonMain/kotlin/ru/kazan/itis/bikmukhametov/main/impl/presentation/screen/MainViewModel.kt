package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.ObserveConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toConversationCardItem
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toItem
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class MainViewModel(
    private val getCabinetUseCase: GetCabinetUseCase,
    private val getProjectListUseCase: GetProjectListUseCase,
    private val getConversationListUseCase: GetConversationListUseCase,
    private val observeConversationListUseCase: ObserveConversationListUseCase,
) : BaseViewModel<MainUiState, MainAction>(MainUiState()) {

    private var currentOffset = 0
    private var isPageLoading = false
    private var isEndReached = false

    init {
        observeConversationsFromCache()
        loadDataSequentially()
    }

    override fun onAction(action: MainAction) {
        when (action) {
            is MainAction.CabinetDropdownChange -> updateState {
                copy(cabinetDropdownExpanded = action.expanded)
            }

            is MainAction.SelectCabinet -> updateState {
                copy(currentCabinet = action.cabinet, cabinetDropdownExpanded = false)
            }

            is MainAction.ProjectDropdownChange -> updateState {
                copy(projectDropdownExpanded = action.expanded)
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

            MainAction.DismissFilterSheet -> updateState {
                copy(filterSheetVisible = false)
            }

            is MainAction.SelectTab -> updateState {
                copy(selectedTab = action.tab).recomputed()
            }

            MainAction.Refresh -> refreshConversations()

            MainAction.ListEndReached -> {
                if (isEndReached || isPageLoading) return@onAction
                viewModelScope.launch { loadConversations() }
            }

            MainAction.RetryClick -> {
                currentOffset = 0
                isEndReached = false
                loadDataSequentially()
            }
        }
    }

    // Подписывается на Room; обновляет allChats при каждом изменении кэша.
    // Если данные пришли из кэша пока ещё идёт загрузка — снимаем shimmer.
    private fun observeConversationsFromCache() {
        viewModelScope.launch {
            observeConversationListUseCase().collect { conversations ->
                val newItems = conversations.map { it.toConversationCardItem() }
                updateState {
                    val stopLoadingEarly = isLoading && newItems.isNotEmpty()
                    copy(
                        allChats = newItems,
                        isLoading = if (stopLoadingEarly) false else isLoading,
                    ).recomputed()
                }
            }
        }
    }

    private fun refreshConversations() {
        viewModelScope.launch {
            updateState {
                copy(
                    isRefreshing = true,
                    loadError = null
                )
            }
            currentOffset = 0
            isEndReached = false
            isPageLoading = false
            loadConversations(reset = true)
            updateState {
                copy(isRefreshing = false)
            }
        }
    }

    // Вычисляет chats из allChats с учётом выбранной вкладки и поискового запроса.
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
                val cabinetUi = cabinetModel.toItem()
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
                val projectsListUi = projectModels.map { it.toItem() }
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
                // allChats обновляется через Room Flow — здесь только пагинация
                if (conversationModels.size < PAGE_SIZE) isEndReached = true
                currentOffset = offsetToLoad + conversationModels.size

                updateState {
                    copy(
                        isLoading = false,
                        isLoadingMore = false,
                        loadError = null,
                    )
                }
            }
            .onFailure { error ->
                updateState {
                    copy(
                        isLoading = false,
                        isLoadingMore = false,
                        // Если в кэше есть данные — не показываем ошибку поверх них
                        loadError = if (allChats.isEmpty()) error.message else null,
                    )
                }
            }

        isPageLoading = false
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
