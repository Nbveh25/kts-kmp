package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ProjectRepository
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

internal class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val spaceProvider: SpaceProvider,
) : ProjectRepository {

    override suspend fun getProject(): Result<List<ProjectModel>> =
        projectDataSource.getProjectList().map { projects ->
            movePersistedProjectFirst(projects)
        }

    /** Проект из DataStore — первый в списке (остальные порядок как в API). */
    private suspend fun movePersistedProjectFirst(projects: List<ProjectModel>): List<ProjectModel> {
        val storedId = spaceProvider.getPersistedProjectId()?.takeIf { it.isNotBlank() }
            ?: return projects
        val index = projects.indexOfFirst { it.id == storedId }
        if (index <= 0) return projects
        return buildList(projects.size) {
            add(projects[index])
            projects.forEachIndexed { i, p -> if (i != index) add(p) }
        }
    }
}
