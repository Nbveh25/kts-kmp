package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi

internal sealed interface ProfileAction {
    data object Logout : ProfileAction
    data object RetryLoad : ProfileAction

    data class ToggleNotifications(val enabled: Boolean) : ProfileAction
    data class ToggleCabinetDropdown(val expanded: Boolean) : ProfileAction
    data class ToggleProjectDropdown(val expanded: Boolean) : ProfileAction
    data class SelectCabinet(val cabinet: CabinetUi) : ProfileAction
    data class SelectProject(val project: ProjectUi) : ProfileAction
}
