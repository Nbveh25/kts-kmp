package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase

internal class GetChatMessagesUseCaseImpl(
    private val chatRepository: ChatRepository,
) : GetChatMessagesUseCase {

    override suspend fun invoke(
        conversationId: String,
        limit: Int,
        fromId: String?,
        fromDate: String?,
    ): Result<List<ChatMessageModel>> {
        Napier.d {
            "$conversationId limit=$limit fromId=$fromId"
        }
        return chatRepository.getMessages(
            conversationId = conversationId,
            limit = limit,
            fromId = fromId,
            fromDate = fromDate,
        )
    }
}
