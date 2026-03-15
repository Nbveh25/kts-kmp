package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import ru.kazan.itis.bikmukhametov.chat.api.model.MessageModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository

internal class ChatRepositoryImpl : ChatRepository {

    override suspend fun getMessages(
        conversationId: String,
        limit: Int,
        offset: Int
    ): Result<List<MessageModel>> = Result.success(emptyList())
}
