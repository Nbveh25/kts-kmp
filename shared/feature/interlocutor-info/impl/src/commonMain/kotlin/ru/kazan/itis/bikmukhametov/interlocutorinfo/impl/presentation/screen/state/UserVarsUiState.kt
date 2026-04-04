package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.state

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField

@Immutable
internal data class UserVarsUiState(
    val items: List<InterlocutorCustomField> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
