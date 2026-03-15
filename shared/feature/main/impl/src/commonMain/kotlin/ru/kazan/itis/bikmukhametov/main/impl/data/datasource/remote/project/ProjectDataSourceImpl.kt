package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet.CabinetResponse
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class ProjectDataSourceImpl(
    private val httpClient: HttpClient
): ProjectDataSource {

    override suspend fun getProjectList(): Result<List<ProjectModel>> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/projects/list"
            )
            val rawBody = response.body<String>()

            Napier.d(tag = "ProjectApi") {
                "RAW API Response: $rawBody"
            }

            response.body<ProjectResponse>()

        }.map { response ->
            response.data.projects.map { projectDto ->
                projectDto.toModel()
            }
        }

        Napier.d(tag = "ProjectApi") { "result: $rawResult" }

        return rawResult.mapApiError("Ошибка загрузки проекта")
    }
}
