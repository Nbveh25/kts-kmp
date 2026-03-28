package ru.kazan.itis.bikmukhametov.api.datasource.remote

interface LoginDataSource {
    suspend fun login(
        email: String,
        password: String,
        captchaToken: String
    ): Result<Unit>
}
