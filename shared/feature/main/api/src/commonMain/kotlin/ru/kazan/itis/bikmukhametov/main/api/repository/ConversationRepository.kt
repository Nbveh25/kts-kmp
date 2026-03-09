package ru.kazan.itis.bikmukhametov.main.api.repository

import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel

interface ConversationRepository {
    suspend fun getConversationList(
        limit: Int,
        offset: Int,
        fromId: String?
    ): Result<List<ConversationModel>>
}
