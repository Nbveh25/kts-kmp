package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class CabinetDataSourceImpl(
    private val httpClient: HttpClient
) : CabinetDataSource {

    override suspend fun getCabinetList(): Result<List<CabinetModel>> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/cabinets/list"
            )
            val body = response.body<CabinetListResponse>()
            Napier.d(tag = "CabinetApi") {
                "cabinets count=${body.data.cabinets.size} first=${body.data.cabinets.firstOrNull()?.name}"
            }
            body
        }.map { response ->
            response.data.cabinets.map { it.toModel() }
        }

        Napier.d(tag = "CabinetApi") { "result: $rawResult" }

        return rawResult.mapApiError("Ошибка загрузки кабинетов")
    }
}
