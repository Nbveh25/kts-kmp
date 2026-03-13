package ru.kazan.itis.bikmukhametov.network.auth.datasource

import ru.kazan.itis.bikmukhametov.network.auth.model.AuthInfoModel

interface AuthDataSource {
    suspend fun fetchAuthInfo(): Result<AuthInfoModel>
}
