package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.model.ProfileItem
import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi

@Immutable
internal data class ProfileUiState(
    val currentCabinet: CabinetUi? = null,
    val cabinets: List<CabinetUi> = emptyList(),
    val cabinetDropdownExpanded: Boolean = false,

    val currentProject: ProjectUi? = null,
    val projects: List<ProjectUi> = emptyList(),
    val projectDropdownExpanded: Boolean = false,

    val isLoading: Boolean = true,
    val profile: ProfileItem? = null,
    val notificationsEnabled: Boolean = true,
    val isLoggingOut: Boolean = false,
    val error: String? = null,
)
