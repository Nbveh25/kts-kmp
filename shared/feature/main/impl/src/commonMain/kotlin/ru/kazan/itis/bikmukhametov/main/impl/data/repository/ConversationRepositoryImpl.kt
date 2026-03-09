package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ConversationDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ConversationRepository

class ConversationRepositoryImpl(
    private val conversationDataSource: ConversationDataSource
) : ConversationRepository {
    override suspend fun getConversationList(
        limit: Int,
        offset: Int,
        fromId: String?
    ): Result<List<ConversationModel>> =
        conversationDataSource.getConversationList(
            limit = limit,
            offset = offset,
            fromId = fromId
        )
}
