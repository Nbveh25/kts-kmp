package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.datasource

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList

interface InterlocutorDataSource {

    /**
     * @param chatId параметр `chat_id` (Mongo `_id` канала).
     * @param userId параметр `user_id` (Mongo `_id` пользователя).
     */
    suspend fun getUserVars(chatId: String, userId: String): Result<List<InterlocutorCustomField>>

    /** @param userId параметр `user_id` (Mongo `_id` пользователя). */
    suspend fun getUserLists(userId: String): Result<List<InterlocutorUserList>>

    /** @param userId параметр `user_id` (Mongo `_id` пользователя). */
    suspend fun getUserChats(userId: String): Result<List<InterlocutorUserChat>>

    /**
     * @param chatId параметр `chat_id` (Mongo `_id` канала).
     * @param userId параметр `user_id` (Mongo `_id` пользователя).
     */
    suspend fun getUserPlannedEvents(chatId: String, userId: String): Result<List<InterlocutorPlannedEvent>>
}
