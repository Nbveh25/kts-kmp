package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatWebSocketDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatWebSocketRepository

class ChatWebSocketRepositoryImpl(
    private val dataSource: ChatWebSocketDataSource,
) : ChatWebSocketRepository {
    override fun observeMessages(conversationId: String): Flow<ChatMessageModel> =
        dataSource.observeMessages(conversationId)
}
