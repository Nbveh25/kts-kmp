package ru.kazan.itis.bikmukhametov.chat.api.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.MessageModel

interface GetChatMessagesUseCase {

    suspend operator fun invoke(conversationId: String, limit: Int, offset: Int): Result<List<MessageModel>>
}
