package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel

@Immutable
data class CabinetUi(
    val id: String,
    val displayName: String
)

fun CabinetModel.toUi() = CabinetUi(
    id = id,
    displayName = name
)
