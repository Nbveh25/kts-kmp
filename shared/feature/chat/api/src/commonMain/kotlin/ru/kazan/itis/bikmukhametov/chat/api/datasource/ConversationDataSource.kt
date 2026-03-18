package ru.kazan.itis.bikmukhametov.chat.api.datasource

import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel

interface ConversationDataSource {
    suspend fun getConversation(conversationId: String): Result<ConversationModel>
}
