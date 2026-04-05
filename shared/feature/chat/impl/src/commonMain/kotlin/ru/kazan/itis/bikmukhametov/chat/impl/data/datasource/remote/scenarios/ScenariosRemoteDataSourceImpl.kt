package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.scenarios

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ScenariosDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class ScenariosRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : ScenariosDataSource {

    override suspend fun getScenariosList(
        kind: String,
        limit: Int,
        offset: Int,
    ): Result<ScenariosListResult> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/scenarios/list",
            ) {
                url {
                    parameters.append("kind", kind)
                    parameters.append("limit", limit.toString())
                    parameters.append("offset", offset.toString())
                }
            }

            val body = response.body<ScenariosListApiResponse>()
            require(body.status == "ok") { "scenarios/list: status=${body.status}" }
            val data = body.data
                ?: throw IllegalStateException("scenarios/list: пустой data")
            data.toResult()
        }

        Napier.d(tag = "ChatApi") {
            "getScenariosList kind=$kind limit=$limit offset=$offset success=${rawResult.isSuccess}"
        }

        return rawResult.mapApiError("Ошибка загрузки сценариев")
    }
}
