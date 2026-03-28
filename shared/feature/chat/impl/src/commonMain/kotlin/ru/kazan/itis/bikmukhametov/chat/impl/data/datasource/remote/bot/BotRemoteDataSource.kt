package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.bot

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.kazan.itis.bikmukhametov.chat.api.datasource.BotDataSource
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

class BotRemoteDataSource(
    private val httpClient: HttpClient
) : BotDataSource {

    override suspend fun startBot(conversationId: String): Result<Unit> {
        val id = conversationId.toLongOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid conversationId: $conversationId"))
        val rawResult = runCatchingCancelable {
            httpClient.post(urlString = BuildKonfig.BASE_URL + "/api/conversations/start_bot") {
                setBody(StartBotRequest(conversationId = id, blockId = null))
            }.body<Unit>()
        }
        rawResult.onSuccess { Napier.d { "Start Bot OK conversationId=$conversationId" } }
        return rawResult.mapApiError("Ошибка старта бота")
    }

    override suspend fun stopBot(conversationId: String): Result<Unit> {
        val id = conversationId.toLongOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid conversationId: $conversationId"))
        val rawResult = runCatchingCancelable {
            httpClient.post(urlString = BuildKonfig.BASE_URL + "/api/conversations/stop_bot") {
                setBody(StopBotRequest(conversationId = id))
            }.body<Unit>()
        }
        rawResult.onSuccess { Napier.d { "Stop Bot OK conversationId=$conversationId" } }
        return rawResult.mapApiError("Ошибка остановки бота")
    }
}
