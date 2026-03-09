package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectUseCase

class GetProjectUseCaseImpl: GetProjectUseCase {
    override suspend fun invoke(): Result<ProjectModel> {
        TODO("Not yet implemented")
    }
}