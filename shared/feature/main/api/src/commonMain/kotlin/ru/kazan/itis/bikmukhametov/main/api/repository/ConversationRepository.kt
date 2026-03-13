package ru.kazan.itis.bikmukhametov.main.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel

interface ConversationRepository {

    fun observeConversations(): Flow<List<ConversationModel>>

    suspend fun getConversationList(
        limit: Int,
        offset: Int,
        fromId: String?
    ): Result<List<ConversationModel>>
}
