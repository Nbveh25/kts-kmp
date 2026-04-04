package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent

fun interface GetUserPlannedEventsUseCase {

    /**
     * @param chatId параметр `chat_id` (Mongo `_id` канала).
     * @param userId параметр `user_id` (Mongo `_id` пользователя).
     */
    suspend operator fun invoke(chatId: String, userId: String): Result<List<InterlocutorPlannedEvent>>
}
