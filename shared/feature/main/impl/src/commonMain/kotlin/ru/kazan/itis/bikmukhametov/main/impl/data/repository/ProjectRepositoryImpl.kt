package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ProjectRepository
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

internal class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val spaceProvider: SpaceProvider
) : ProjectRepository {

    override suspend fun getProject(): Result<List<ProjectModel>> =
        projectDataSource.getProject()
            .onSuccess { projectModels ->
                projectModels.firstOrNull()?.let { first ->
                    val currentCabinet = spaceProvider.cabinet.value ?: ""
                    spaceProvider.setSpace(currentCabinet, first.id)
                }
            }

}
