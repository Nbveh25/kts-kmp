package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.conversation

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ConversationDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project.ProjectResponse
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project.toModel
import ru.kazan.itis.bikmukhametov.network.error.mapApiError

class ConversationDataSourceImpl(
    private val httpClient: HttpClient
) : ConversationDataSource {

    override suspend fun getConversationList(
        limit: Int,
        offset: Int,
        fromId: String?
    ): Result<List<ConversationModel>> {
        val rawResult = runCatching {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/conversations/list"
            ) {
                url {
                    parameters.append("limit", limit.toString())
                    parameters.append("offset", offset.toString())

                    // fromId может быть null, добавляем только если он не null
                    fromId?.let {
                        parameters.append("fromId", it)
                    }
                }
                
                contentType(ContentType.Application.Json)
                header("Accept", "application/json, text/plain, */*")
            }
            
            val rawBody = response.body<String>()
            println("CONVERSATION RAW API Response: $rawBody")

            response.body<ConversationResponse>()
        }.map { response ->
            response.data.conversations.map { conversationDto ->
                conversationDto.toModel()
            }
        }

        return rawResult.mapApiError("Ошибка загрузки чатов")
    }
}
