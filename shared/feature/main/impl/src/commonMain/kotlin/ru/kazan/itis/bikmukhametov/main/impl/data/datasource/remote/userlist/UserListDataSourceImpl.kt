package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.userlist

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.UserListDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class UserListDataSourceImpl(
    private val httpClient: HttpClient,
) : UserListDataSource {

    override suspend fun getUserLists(): Result<List<UserListModel>> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/lists/list",
            ) {
                url { parameters.append("type", "users") }
            }

            response.body<UserListResponse>()
        }.map { response ->
            response.data.lists.map { it.toModel() }
        }

        Napier.d(tag = "UserListApi") { "loaded user lists: $rawResult" }
        return rawResult.mapApiError("Ошибка загрузки списков пользователей")
    }
}
