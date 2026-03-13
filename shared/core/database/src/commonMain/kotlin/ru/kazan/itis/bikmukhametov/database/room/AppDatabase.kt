package ru.kazan.itis.bikmukhametov.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kazan.itis.bikmukhametov.database.room.conversation.ConversationDao
import ru.kazan.itis.bikmukhametov.database.room.conversation.ConversationEntity

@Database(
    entities = [
        ConversationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun conversationDao(): ConversationDao

}
