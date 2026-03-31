package ru.kazan.itis.bikmukhametov.profile.impl.presentation.model

import ru.kazan.itis.bikmukhametov.main.api.model.space.ProjectModel
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi

fun ProjectModel.toItem() = ProjectUi(
    id = id,
    displayName = name,
)
