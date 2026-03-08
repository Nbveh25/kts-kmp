package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.SpaceUi

internal sealed interface MainAction {
    data object ToggleSpaceDropdown : MainAction
    data class SelectSpace(val space: SpaceUi) : MainAction
    data object ToggleSearch : MainAction
    data class SearchQueryChanged(val query: String) : MainAction
    data object ToggleFilterSheet : MainAction
    data class SelectTab(val tab: ChatListTab) : MainAction
}
