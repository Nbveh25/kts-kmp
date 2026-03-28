package ru.kazan.itis.bikmukhametov.chat.api.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel

interface GetConversationByIdUseCase {
    suspend operator fun invoke(conversationId: String): Result<ConversationModel>
}
