package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi

fun CabinetModel.toItem() = CabinetUi(
    id = id,
    displayName = name
)
