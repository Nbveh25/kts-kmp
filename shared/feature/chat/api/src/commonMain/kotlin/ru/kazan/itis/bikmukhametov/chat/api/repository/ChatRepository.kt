package ru.kazan.itis.bikmukhametov.chat.api.repository

import ru.kazan.itis.bikmukhametov.chat.api.model.MessageModel

interface ChatRepository {

    suspend fun getMessages(conversationId: String, limit: Int, offset: Int): Result<List<MessageModel>>
}
