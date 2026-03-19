package ru.kazan.itis.bikmukhametov.chat.api.datasource

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface ChatDataSource {
    suspend fun getMessageList(
        conversationId: Long,
        limit: Int,
        fromId: String? = null,
        fromDate: String? = null,
    ): Result<List<ChatMessageModel>>

    suspend fun sendMessage(conversationId: Long, messageText: String): Result<Unit>
}
