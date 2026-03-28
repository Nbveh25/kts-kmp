package ru.kazan.itis.bikmukhametov.chat.api.usecase

interface StartBotUseCase {
    suspend operator fun invoke(conversationId: String): Result<Unit>
}
