package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel

@Immutable
data class ProjectUi(
    val id: String,
    val displayName: String
)

fun ProjectModel.toUi() = ProjectUi(
    id = id,
    displayName = name,
)
