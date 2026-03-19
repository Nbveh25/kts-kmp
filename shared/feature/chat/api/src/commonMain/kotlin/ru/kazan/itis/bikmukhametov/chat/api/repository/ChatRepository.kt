package ru.kazan.itis.bikmukhametov.chat.api.repository

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface ChatRepository {

    suspend fun getMessages(
        conversationId: String,
        limit: Int,
        fromId: String? = null,
        fromDate: String? = null,
    ): Result<List<ChatMessageModel>>

    suspend fun sendMessage(conversationId: String, messageText: String): Result<Unit>
}
