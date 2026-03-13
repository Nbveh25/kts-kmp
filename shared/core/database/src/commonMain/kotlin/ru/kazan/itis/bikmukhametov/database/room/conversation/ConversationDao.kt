package ru.kazan.itis.bikmukhametov.database.room.conversation

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.database.room.conversation.ConversationEntity

@Dao
abstract class ConversationDao {

    @Query("SELECT * FROM conversations ORDER BY dateUpdated DESC")
    abstract fun observeAll(): Flow<List<ConversationEntity>>

    @Upsert
    abstract suspend fun upsertAll(conversations: List<ConversationEntity>)

    @Query("DELETE FROM conversations")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun clearAndInsertAll(conversations: List<ConversationEntity>) {
        deleteAll()
        upsertAll(conversations)
    }
}
