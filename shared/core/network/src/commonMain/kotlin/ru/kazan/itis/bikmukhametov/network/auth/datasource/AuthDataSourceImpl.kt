package ru.kazan.itis.bikmukhametov.network.auth.datasource

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.network.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.auth.mapper.toModel
import ru.kazan.itis.bikmukhametov.network.auth.model.AuthInfoModel
import ru.kazan.itis.bikmukhametov.network.auth.response.AuthInfoResponse
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class AuthDataSourceImpl(
    private val httpClient: HttpClient,
) : AuthDataSource {

    override suspend fun fetchAuthInfo(): Result<AuthInfoModel> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/auth/info"
            )
            val body = response.body<AuthInfoResponse>()
            Napier.d(tag = "AuthApi") { "auth/info parsed OK, status=${response.status}" }
            body
        }.map { response ->
            response.toModel()
        }

        Napier.d(tag = "AuthApi") { "result: $rawResult" }

        return rawResult.mapApiError("Ошибка получения информации об авторизации")
    }
}
