package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField

fun interface GetUserVarsUseCase {

    suspend operator fun invoke(chatId: String, userId: String): Result<List<InterlocutorCustomField>>
}
