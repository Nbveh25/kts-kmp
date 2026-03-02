package ru.kazan.itis.bikmukhametov.api.datasource.remote

interface LoginDataSource {
    suspend fun login(username: String, password: String): Result<Unit>
}
