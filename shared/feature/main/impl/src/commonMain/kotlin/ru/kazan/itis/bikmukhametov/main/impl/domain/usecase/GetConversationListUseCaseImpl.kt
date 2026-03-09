package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetConversationListUseCase

class GetConversationListUseCaseImpl(
    private val conversationRepository: ConversationRepository
) : GetConversationListUseCase {
    override suspend fun invoke(
        limit: Int,
        offset: Int,
        fromId: String?
    ): Result<List<ConversationModel>> =
        conversationRepository.getConversationList(
            limit = limit,
            offset = offset,
            fromId = fromId
        )
}