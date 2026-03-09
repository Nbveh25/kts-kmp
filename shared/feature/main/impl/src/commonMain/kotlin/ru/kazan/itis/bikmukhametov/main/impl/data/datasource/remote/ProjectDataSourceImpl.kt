package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel

internal class ProjectDataSourceImpl: ProjectDataSource {

    override suspend fun getProject(): Result<ProjectModel> {
        TODO("Not yet implemented")
    }
}
