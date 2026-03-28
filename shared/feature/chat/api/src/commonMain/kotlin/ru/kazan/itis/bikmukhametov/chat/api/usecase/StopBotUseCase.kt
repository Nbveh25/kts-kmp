package ru.kazan.itis.bikmukhametov.chat.api.usecase

interface StopBotUseCase {
    suspend operator fun invoke(conversationId: String): Result<Unit>
}
