package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.repository

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.datasource.InterlocutorDataSource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository.InterlocutorRepository

internal class InterlocutorRepositoryImpl(
    private val dataSource: InterlocutorDataSource,
) : InterlocutorRepository {

    override suspend fun getUserVars(chatId: String, userId: String): Result<List<InterlocutorCustomField>> =
        dataSource.getUserVars(chatId, userId)

    override suspend fun getUserLists(userId: String): Result<List<InterlocutorUserList>> =
        dataSource.getUserLists(userId)

    override suspend fun getUserChats(userId: String): Result<List<InterlocutorUserChat>> =
        dataSource.getUserChats(userId)

    override suspend fun getUserPlannedEvents(chatId: String, userId: String): Result<List<InterlocutorPlannedEvent>> =
        dataSource.getUserPlannedEvents(chatId, userId)
}
