package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList

interface InterlocutorRepository {

    suspend fun getUserVars(chatId: String, userId: String): Result<List<InterlocutorCustomField>>

    suspend fun getUserLists(userId: String): Result<List<InterlocutorUserList>>

    suspend fun getUserChats(userId: String): Result<List<InterlocutorUserChat>>

    suspend fun getUserPlannedEvents(chatId: String, userId: String): Result<List<InterlocutorPlannedEvent>>
}
