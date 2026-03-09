package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.network.error.mapApiError

class CabinetDataSourceImpl(
    private val httpClient: HttpClient
) : CabinetDataSource {

    override suspend fun getCabinet(): Result<CabinetModel> { // TODO потом список сделать
        val rawResult = runCatching {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/cabinets/get_by_domain"
            ) {
                url {
                    parameters.append("domain", BuildKonfig.CABINET_DOMAIN)
                }

                contentType(ContentType.Application.Json)
                header("Accept", "application/json, text/plain, */*")
            }

            response.body<CabinetResponse>()

        }.map { response ->
            println("cabinetName: ${response.data.cabinet.name}")
            response.data.cabinet.toModel()
        }

        println("cabinet: $rawResult.")

        return rawResult.mapApiError("Ошибка загрузки кабинета")
    }
}