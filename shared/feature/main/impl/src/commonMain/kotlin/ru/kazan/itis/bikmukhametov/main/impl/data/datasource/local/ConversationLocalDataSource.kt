package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kazan.itis.bikmukhametov.database.room.conversation.ConversationDao
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel

internal class ConversationLocalDataSource(
    private val conversationDao: ConversationDao
) {
    fun observeConversations(): Flow<List<ConversationModel>> =
        conversationDao.observeAll().map { entities ->
            entities.map { it.toModel() }
        }

    suspend fun saveConversations(conversations: List<ConversationModel>) {
        conversationDao.upsertAll(conversations.map { it.toEntity() })
    }

    suspend fun replaceConversations(conversations: List<ConversationModel>) {
        conversationDao.clearAndInsertAll(conversations.map { it.toEntity() })
    }
}
