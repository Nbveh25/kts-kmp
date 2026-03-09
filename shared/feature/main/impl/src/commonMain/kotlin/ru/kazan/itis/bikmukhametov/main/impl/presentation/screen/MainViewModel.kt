package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ChatCardUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ProjectUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.SocialBadge
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.toUi
import kotlin.collections.emptyList

internal class MainViewModel(
    private val getCabinetUseCase: GetCabinetUseCase,
    private val getProjectListUseCase: GetProjectListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    init {
        // экран - загрузка
        viewModelScope.launch {

            val cabinetDeferred = async { getCabinetUseCase() }
            val projectListDeferred = async { getProjectListUseCase() }

            val cabinetResult = cabinetDeferred.await()
            val projectListResult = projectListDeferred.await()

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
            
        }
    }

    private fun createInitialState(): MainUiState {
        val chats = listOf(
            ChatCardUi(
                id = "1",
                avatarUrl = null,
                socialBadge = SocialBadge.TG,
                name = "Иван Петров",
                lastMessageText = "Добрый день, подскажите по тарифу",
                timeOrDate = "12:30",
                unreadCount = 2
            ),
            ChatCardUi(
                id = "2",
                avatarUrl = null,
                socialBadge = SocialBadge.WA,
                name = "Мария Сидорова",
                lastMessageText = "Спасибо, всё получила!",
                timeOrDate = "Вчера",
                unreadCount = 0
            ),
            ChatCardUi(
                id = "3",
                avatarUrl = null,
                socialBadge = SocialBadge.TG,
                name = "Чат поддержки",
                lastMessageText = "Оператор подключится в течение 5 минут",
                timeOrDate = "Пн",
                unreadCount = 5
            ),
            ChatCardUi(
                id = "4",
                avatarUrl = null,
                socialBadge = SocialBadge.WA,
                name = "Алексей К.",
                lastMessageText = "Когда будет готов отчёт?",
                timeOrDate = "09:15",
                unreadCount = 1
            )
        )
        // На момент инициализации _state ещё нет, поэтому используем только константные значения.
        // Кабинеты подтянутся асинхронно в init{} через usecase.
        return MainUiState(
            currentCabinet = null,
            cabinets = emptyList(),
            currentProject = null,
            projects = emptyList(),
            chats = chats
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
