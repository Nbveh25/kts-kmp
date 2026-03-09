package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel

interface GetProjectUseCase {

    suspend operator fun invoke(): Result<ProjectModel>

}