package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.blocks

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.chat.api.datasource.BlocksDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class BlocksRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : BlocksDataSource {

    override suspend fun getBlocksList(scenarioId: String): Result<BlocksListResult> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/blocks/list",
            ) {
                url {
                    parameters.append("scenario_id", scenarioId)
                }
            }

            val body = response.body<BlocksListApiResponse>()
            require(body.status == "ok") { "blocks/list: status=${body.status}" }
            val data = body.data
                ?: error("blocks/list: пустой data")
            data.toResult()
        }

        Napier.d(tag = "ChatApi") {
            "getBlocksList scenarioId=$scenarioId success=${rawResult.isSuccess}"
        }

        return rawResult.mapApiError("Ошибка загрузки блоков")
    }
}
