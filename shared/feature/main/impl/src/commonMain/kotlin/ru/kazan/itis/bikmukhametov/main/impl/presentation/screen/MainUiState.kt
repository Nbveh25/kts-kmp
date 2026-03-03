package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.MainItemUi

@Immutable
internal data class MainUiState(
    val items: List<MainItemUi> = emptyList()
)
