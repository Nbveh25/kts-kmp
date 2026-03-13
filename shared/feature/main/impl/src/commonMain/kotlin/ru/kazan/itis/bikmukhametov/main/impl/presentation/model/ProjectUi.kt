package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi

fun ProjectModel.toUi() = ProjectUi(
    id = id,
    displayName = name,
)
