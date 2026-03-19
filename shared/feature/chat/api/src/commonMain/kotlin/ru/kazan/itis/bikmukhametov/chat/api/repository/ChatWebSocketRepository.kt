package ru.kazan.itis.bikmukhametov.chat.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface ChatWebSocketRepository {
    fun observeMessages(conversationId: String): Flow<ChatMessageModel>
}
