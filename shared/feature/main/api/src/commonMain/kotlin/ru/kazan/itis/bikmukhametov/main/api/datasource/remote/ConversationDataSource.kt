package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel


interface ConversationDataSource {
    suspend fun getConversationList(
        limit: Int,
        offset: Int,
        fromId: String?
    ): Result<List<ConversationModel>>
}