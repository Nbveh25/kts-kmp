package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.ObserveConversationListUseCase

internal class ObserveConversationListUseCaseImpl(
    private val conversationRepository: ConversationRepository,
) : ObserveConversationListUseCase {

    override fun invoke(): Flow<List<ConversationModel>> =
        conversationRepository.observeConversations()
}
