package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.BillingModel
import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.network.error.mapApiError

class CabinetDataSourceImpl(
    private val httpClient: HttpClient
) : CabinetDataSource {

    override suspend fun getCabinet(): Result<CabinetModel> {
        val rawResult = runCatching {
            val response = httpClient.get(
                BuildKonfig.BASE_URL +
                    "/api/cabinets/get_by_domain?domain=${BuildKonfig.CABINET_DOMAIN}"
            ) {
                contentType(ContentType.Application.Json)

                header("Accept", "application/json, text/plain, */*")
            }

            val cookieHeader = response.call.request.headers["Cookie"]
            println("Cabinet request -> URL=${response.call.request.url}, Cookie=$cookieHeader")

            response.body<CabinetResponse>()


        }.map { response ->
            val cabinet = response.data.cabinet
            CabinetModel(
                id = cabinet.id,
                host = cabinet.host,
                domain = cabinet.domain,
                name = cabinet.name,
                category = cabinet.category,
                billing = BillingModel(
                    balance = cabinet.billing.balance,
                    trialUntil = cabinet.billing.trialUntil,
                    tariff = cabinet.billing.tariff,
                    hasLinkedCard = cabinet.billing.hadLinkedCard
                ),
                shouldPay = cabinet.shouldPay,
                role = cabinet.cabinetRole,
                permissions = cabinet.permissions,
                createdAt = cabinet.dateCreated,
                createdBy = cabinet.createdBy
            )


        }

        println("cabinet: $rawResult.")

        return rawResult.mapApiError("Ошибка загрузки кабинета")
    }
}