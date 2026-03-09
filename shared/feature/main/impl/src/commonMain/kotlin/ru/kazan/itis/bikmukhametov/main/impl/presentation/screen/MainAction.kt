package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ProjectUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.CabinetUi

internal sealed interface MainAction {
    data object ToggleCabinetDropdown : MainAction
    data class SelectCabinet(val cabinet: CabinetUi) : MainAction
    data object ToggleProjectDropdown : MainAction
    data class SelectProject(val project: ProjectUi) : MainAction
    data object ToggleSearch : MainAction
    data class SearchQueryChanged(val query: String) : MainAction
    data object ToggleFilterSheet : MainAction
    data class SelectTab(val tab: ChatListTab) : MainAction
}
