package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ProjectModel
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet.CabinetResponse
import ru.kazan.itis.bikmukhametov.network.error.mapApiError

internal class ProjectDataSourceImpl(
    private val httpClient: HttpClient
): ProjectDataSource {

    override suspend fun getProjectList(): Result<List<ProjectModel>> {
        val rawResult = runCatching {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/projects/list"
            ) {
                contentType(ContentType.Application.Json)
                header("Accept", "application/json, text/plain, */*")
            }
            
            val rawBody = response.body<String>()
            println("RAW API Response: $rawBody")

            response.body<ProjectResponse>()

        }.map { response ->
            response.data.projects.map { projectDto ->
                projectDto.toModel()
            }
        }

        println("project: $rawResult.")

        return rawResult.mapApiError("Ошибка загрузки проекта")
    }
}