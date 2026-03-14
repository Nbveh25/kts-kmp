package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi
import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi

internal sealed interface MainAction {
    data class CabinetDropdownChange(val expanded: Boolean) : MainAction
    data class SelectCabinet(val cabinet: CabinetUi) : MainAction
    data class ProjectDropdownChange(val expanded: Boolean) : MainAction
    data class SelectProject(val project: ProjectUi) : MainAction
    data object ToggleSearch : MainAction
    data class SearchQueryChanged(val query: String) : MainAction
    data object ToggleFilterSheet : MainAction
    data object DismissFilterSheet : MainAction
    data class SelectTab(val tab: ChatListTab) : MainAction
    data object Refresh : MainAction
    data object ListEndReached : MainAction
    data object RetryClick : MainAction
}
