package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository.InterlocutorRepository
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserVarsUseCase

internal class GetUserVarsUseCaseImpl(
    private val repository: InterlocutorRepository,
) : GetUserVarsUseCase {

    override suspend fun invoke(chatId: String, userId: String): Result<List<InterlocutorCustomField>> =
        repository.getUserVars(chatId, userId)
}
