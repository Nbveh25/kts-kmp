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

internal class AuthDataSourceImpl(
    private val httpClient: HttpClient,
) : AuthDataSource {

    override suspend fun fetchAuthInfo(): Result<AuthInfoModel> {
        val rawResult = runCatching {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/auth/info"
            )
            val rawBody = response.body<String>()

            Napier.d(tag = "AuthApi") {
                "RAW API Response: $rawBody"
            }

            response.body<AuthInfoResponse>()

        }.map { response ->
            response.toModel()
        }

        Napier.d(tag = "AuthApi") { "result: $rawResult" }

        return rawResult.mapApiError("Ошибка получения информации об авторизации")
    }
}
