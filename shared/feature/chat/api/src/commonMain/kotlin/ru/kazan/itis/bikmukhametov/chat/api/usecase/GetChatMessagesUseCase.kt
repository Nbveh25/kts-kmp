package ru.kazan.itis.bikmukhametov.chat.api.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface GetChatMessagesUseCase {

    suspend operator fun invoke(
        conversationId: String,
        limit: Int,
        fromId: String? = null,
        fromDate: String? = null,
    ): Result<List<ChatMessageModel>>
}
