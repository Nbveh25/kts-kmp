package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import ru.kazan.itis.bikmukhametov.api.datasource.remote.LoginDataSource

internal class LoginDataSourceImpl: LoginDataSource {
    override suspend fun login(
        username: String,
        password: String
    ): Result<Unit> {
        // Мока
        return if (username == "user" && password == "pass") {
            Result.success(Unit)
        } else {
            Result.failure(Exception(INVALID_CREDENTIALS))
        }
    }

    companion object {
        private const val INVALID_CREDENTIALS = "Неверные данные"
    }
    
}
