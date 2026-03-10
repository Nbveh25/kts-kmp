package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel

interface GetProjectListUseCase {

    suspend operator fun invoke(): Result<List<ProjectModel>>

}
