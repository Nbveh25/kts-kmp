package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList

@Immutable
internal data class UserListsUiState(
    val items: List<InterlocutorUserList> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
