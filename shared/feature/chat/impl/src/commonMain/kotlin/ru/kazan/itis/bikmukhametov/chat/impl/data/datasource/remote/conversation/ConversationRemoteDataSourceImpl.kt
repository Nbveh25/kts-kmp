package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ConversationDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

class ConversationRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ConversationDataSource {

    override suspend fun getConversation(
        conversationId: String
    ): Result<ConversationModel> {

        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/conversations/get_conversation"
            ) {
                url {
                    parameters.append("id", conversationId)
                }
            }

            val data = response.body<GetConversationApiResponse>().data
                ?: throw IllegalArgumentException("get_conversation returned empty data")
            data.toModel(fallbackConversationIdFromRequest = conversationId)
        }

        Napier.d(tag = "ChatApi") {
            "getConversation conversationId=$conversationId success=${rawResult.isSuccess}"
        }

        return rawResult.mapApiError("Ошибка загрузки чата")
    }

}
