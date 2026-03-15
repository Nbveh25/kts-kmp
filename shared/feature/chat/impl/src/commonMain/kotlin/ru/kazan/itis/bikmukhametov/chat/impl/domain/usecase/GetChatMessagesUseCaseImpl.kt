package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.MessageModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase

internal class GetChatMessagesUseCaseImpl(
    private val chatRepository: ChatRepository,
) : GetChatMessagesUseCase {

    override suspend fun invoke(
        conversationId: String,
        limit: Int,
        offset: Int
    ): Result<List<MessageModel>> = chatRepository.getMessages(conversationId, limit, offset)
}
