package ru.kazan.itis.bikmukhametov.chat.api.datasource

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface ChatWebSocketDataSource {
    fun observeMessages(conversationId: String): Flow<ChatMessageModel>
}
