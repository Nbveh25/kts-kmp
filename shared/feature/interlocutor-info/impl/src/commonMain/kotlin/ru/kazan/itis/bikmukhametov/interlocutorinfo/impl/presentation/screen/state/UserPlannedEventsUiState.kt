package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent

@Immutable
internal data class UserPlannedEventsUiState(
    val items: List<InterlocutorPlannedEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
