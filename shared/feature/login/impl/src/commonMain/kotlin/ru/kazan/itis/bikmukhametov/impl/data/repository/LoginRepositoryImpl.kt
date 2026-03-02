package ru.kazan.itis.bikmukhametov.impl.data.repository

import ru.kazan.itis.bikmukhametov.api.datasource.remote.LoginDataSource
import ru.kazan.itis.bikmukhametov.api.repository.LoginRepository

internal class LoginRepositoryImpl(
    private val loginDataSource: LoginDataSource
): LoginRepository {
    override suspend fun login(username: String, password: String): Result<Unit> {
        return loginDataSource.login(
            username = username,
            password = password
        )
    }
}