package ru.kazan.itis.bikmukhametov.main.api.repository

import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel

interface ProjectRepository {
    suspend fun getProject(): Result<List<ProjectModel>>
}