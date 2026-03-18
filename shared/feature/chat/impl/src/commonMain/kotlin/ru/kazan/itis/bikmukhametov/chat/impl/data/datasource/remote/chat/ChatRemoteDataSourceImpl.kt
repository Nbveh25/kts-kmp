package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class ChatRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ChatDataSource {

    override suspend fun getMessageList(
        conversationId: Long,
        limit: Int,
        fromId: String?,
        fromDate: String?,
    ): Result<List<ChatMessageModel>> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/conversations/list_messages"
            ) {
                url {
                    parameters.append("conversation_id", conversationId.toString())
                    parameters.append("limit", limit.toString())
                    if (fromId != null) parameters.append("from_id", fromId)
                    if (fromDate != null) parameters.append("from_date", fromDate)
                }
            }

            response.body<ChatMessageResponse>()
        }.map { response ->
            Napier.d(tag = "ChatApi") {
                "loaded ${response.data.messages.size} messages for conversation $conversationId (fromId=$fromId)"
            }
            response.data.messages.map { it.toModel() }
        }

        return rawResult
            .mapApiError("Ошибка загрузки сообщений чата")
    }
}
