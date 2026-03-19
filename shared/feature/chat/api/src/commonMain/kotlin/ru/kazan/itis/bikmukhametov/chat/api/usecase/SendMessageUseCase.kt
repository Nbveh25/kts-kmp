package ru.kazan.itis.bikmukhametov.chat.api.usecase

interface SendMessageUseCase {
    suspend operator fun invoke(conversationId: String, text: String): Result<Unit>
}