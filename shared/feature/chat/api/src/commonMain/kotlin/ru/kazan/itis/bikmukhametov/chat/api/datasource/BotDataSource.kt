package ru.kazan.itis.bikmukhametov.chat.api.datasource

interface BotDataSource {
    suspend fun startBot(conversationId: String): Result<Unit>
    suspend fun stopBot(conversationId: String): Result<Unit>
}