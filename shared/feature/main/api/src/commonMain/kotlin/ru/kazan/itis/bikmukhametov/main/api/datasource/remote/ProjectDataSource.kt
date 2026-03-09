package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel

/* Project контракт */
interface ProjectDataSource {
    suspend fun getProject(): Result<List<ProjectModel>>
}
