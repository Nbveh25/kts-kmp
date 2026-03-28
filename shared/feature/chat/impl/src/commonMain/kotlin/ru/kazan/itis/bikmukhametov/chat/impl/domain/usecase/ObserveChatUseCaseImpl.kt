package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatWebSocketRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.ObserveChatUseCase

class ObserveChatUseCaseImpl(
    private val repository: ChatWebSocketRepository,
) : ObserveChatUseCase {
    override fun invoke(conversationId: String): Flow<ChatMessageModel> =
        repository.observeMessages(conversationId)
}
