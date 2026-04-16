package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList

fun interface GetUserListsUseCase {

    /** @param userId параметр `user_id` (Mongo `_id` пользователя). */
    suspend operator fun invoke(userId: String): Result<List<InterlocutorUserList>>
}
