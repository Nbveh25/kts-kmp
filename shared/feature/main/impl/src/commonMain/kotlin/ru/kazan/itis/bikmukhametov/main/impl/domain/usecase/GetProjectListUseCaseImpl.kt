package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.space.ProjectModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ProjectRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase

class GetProjectListUseCaseImpl(
    private val projectRepository: ProjectRepository
): GetProjectListUseCase {

    override suspend fun invoke(): Result<List<ProjectModel>> =
        projectRepository.getProject()

}
