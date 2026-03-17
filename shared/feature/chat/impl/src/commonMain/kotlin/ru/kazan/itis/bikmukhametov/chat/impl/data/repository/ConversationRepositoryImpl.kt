package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import ru.kazan.itis.bikmukhametov.chat.api.datasource.ConversationDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ConversationRepository

class ConversationRepositoryImpl(
    private val conversationDataSource: ConversationDataSource
) : ConversationRepository {

    override suspend fun getConversation(conversationId: String): Result<ConversationModel> =
        conversationDataSource.getConversation(conversationId)

}
