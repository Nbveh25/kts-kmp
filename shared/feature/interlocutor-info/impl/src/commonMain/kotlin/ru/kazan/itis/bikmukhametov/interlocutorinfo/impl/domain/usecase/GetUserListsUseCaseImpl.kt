package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository.InterlocutorRepository
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserListsUseCase

internal class GetUserListsUseCaseImpl(
    private val repository: InterlocutorRepository,
) : GetUserListsUseCase {

    override suspend fun invoke(userId: String): Result<List<InterlocutorUserList>> =
        repository.getUserLists(userId)
}
