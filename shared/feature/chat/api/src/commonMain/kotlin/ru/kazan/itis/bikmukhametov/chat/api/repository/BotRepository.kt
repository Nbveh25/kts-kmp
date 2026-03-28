package ru.kazan.itis.bikmukhametov.chat.api.repository

interface BotRepository {
    suspend fun startBot(conversationId: String): Result<Unit>
    suspend fun stopBot(conversationId: String): Result<Unit>
}
