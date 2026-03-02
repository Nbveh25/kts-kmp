package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.MainItemUi

internal data class MainUiState(
    val items: List<MainItemUi> = emptyList()
)
