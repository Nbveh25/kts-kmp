package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage.SendMessageRequest
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage.SendMessageResponse
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class ChatRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ChatDataSource {

    override suspend fun sendMessage(conversationId: Long, messageText: String): Result<Unit> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.post(
                urlString = BuildKonfig.BASE_URL + "/api/conversations/send_message"
            ) {
                setBody(
                    SendMessageRequest(
                        conversationId = conversationId,
                        messageText = messageText,
                        attachments = emptyList()
                    )
                )
            }
            response.body<SendMessageResponse>()
        }.map { response ->
            Napier.d(tag = "ChatApi") {
                "send_message: status=${response.status} conversationId=$conversationId"
            }
        }
        return rawResult.mapApiError("Ошибка отправки сообщения")
    }

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
            val list = response.data.messageList
            Napier.d(tag = "ChatApi") {
                "loaded ${list.size} messages for conversation $conversationId (fromId=$fromId)"
            }
            list.map { it.toModel() }
        }

        return rawResult
            .mapApiError("Ошибка загрузки сообщений чата")
    }
}
