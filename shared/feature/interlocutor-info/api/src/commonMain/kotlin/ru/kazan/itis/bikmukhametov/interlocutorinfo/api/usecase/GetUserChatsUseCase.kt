package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat

fun interface GetUserChatsUseCase {

    /** @param userId параметр `user_id` (Mongo `_id` пользователя). */
    suspend operator fun invoke(userId: String): Result<List<InterlocutorUserChat>>
}
