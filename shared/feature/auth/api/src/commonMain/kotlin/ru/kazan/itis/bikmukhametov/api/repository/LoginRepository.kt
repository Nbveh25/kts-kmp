package ru.kazan.itis.bikmukhametov.api.repository

interface LoginRepository {
    suspend fun login(username: String, password: String): Result<Unit>
}
