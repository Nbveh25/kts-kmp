package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase

class GetConversationByIdUseCaseImpl(
    private val conversationRepository: ConversationRepository
) : GetConversationByIdUseCase {
    override suspend operator fun invoke(conversationId: String): Result<ConversationModel> =
        conversationRepository.getConversation(conversationId)
}
