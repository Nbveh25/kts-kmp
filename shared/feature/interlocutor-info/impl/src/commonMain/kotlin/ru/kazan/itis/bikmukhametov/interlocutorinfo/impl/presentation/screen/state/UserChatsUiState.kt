package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat

@Immutable
internal data class UserChatsUiState(
    val items: List<InterlocutorUserChat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
