package ru.kazan.itis.bikmukhametov.api.repository

interface LoginRepository {
    suspend fun login(
        email: String,
        password: String,
        captchaToken: String
    ): Result<Unit>
}
