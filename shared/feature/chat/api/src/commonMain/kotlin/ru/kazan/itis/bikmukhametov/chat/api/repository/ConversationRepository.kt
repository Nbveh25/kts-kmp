package ru.kazan.itis.bikmukhametov.chat.api.repository

import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel

interface ConversationRepository {
    suspend fun getConversation(conversationId: String): Result<ConversationModel>
}
