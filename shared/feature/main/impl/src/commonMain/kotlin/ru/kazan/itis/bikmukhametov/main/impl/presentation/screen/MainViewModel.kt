package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ChatCardUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ProjectUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.SocialBadge
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.SpaceUi

internal class MainViewModel(
    private val getCabinetUseCase: GetCabinetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getCabinetUseCase().onSuccess { cabinet ->
                print("cabinet: ${cabinet.id}")
                print("name: ${cabinet.name}")
            }.onFailure {

            }
        }
    }

    private fun createInitialState(): MainUiState {
        val spaces = listOf(
            SpaceUi("1", "Компания А"),
            SpaceUi("2", "Компания Б"),
            SpaceUi("3", "Стартап В")
        )
        val projects = listOf(
            ProjectUi("1", "Проект 1"),
            ProjectUi("2", "Проект 2"),
            ProjectUi("3", "Поддержка")
        )
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
        return MainUiState(
            currentSpace = spaces.first(),
            spaces = spaces,
            currentProject = projects.first(),
            projects = projects,
            chats = chats
        )
    }

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.ToggleSpaceDropdown -> updateState {
                copy(spaceDropdownExpanded = !spaceDropdownExpanded)
            }
            is MainAction.SelectSpace -> updateState {
                copy(currentSpace = action.space, spaceDropdownExpanded = false)
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
        updateState { copy(spaceDropdownExpanded = expanded) }
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
