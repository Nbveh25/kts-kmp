package ru.kazan.itis.bikmukhametov.main.api.repository

interface ProjectRepository {
    suspend fun getProject()
}