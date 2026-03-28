package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import ru.kazan.itis.bikmukhametov.chat.api.datasource.BotDataSource
import ru.kazan.itis.bikmukhametov.chat.api.repository.BotRepository

class BotRepositoryImpl(
    private val botDataSource: BotDataSource
): BotRepository {
    override suspend fun startBot(conversationId: String): Result<Unit> {
        return botDataSource.startBot(conversationId)
    }

    override suspend fun stopBot(conversationId: String): Result<Unit> {
        return botDataSource.stopBot(conversationId)
    }
}
