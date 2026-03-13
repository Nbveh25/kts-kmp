package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ConversationDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.local.ConversationLocalDataSource

internal class ConversationRepositoryImpl(
    private val remoteDataSource: ConversationDataSource,
    private val localDataSource: ConversationLocalDataSource,
) : ConversationRepository {

    override fun observeConversations(): Flow<List<ConversationModel>> =
        localDataSource.observeConversations()

    override suspend fun getConversationList(
        limit: Int,
        offset: Int,
        fromId: String?,
    ): Result<List<ConversationModel>> =
        remoteDataSource.getConversationList(limit, offset, fromId)
            .onSuccess { conversations ->
                if (offset == 0) {
                    localDataSource.replaceConversations(conversations)
                    Napier.d {
                        "Замена в бд: ${conversations.size}"
                    }
                } else {
                    Napier.d {
                        "Сохранение в бд: ${conversations.size}"
                    }
                    localDataSource.saveConversations(conversations)
                }
            }
}
