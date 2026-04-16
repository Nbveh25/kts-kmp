package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository.InterlocutorRepository
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserChatsUseCase

internal class GetUserChatsUseCaseImpl(
    private val repository: InterlocutorRepository,
) : GetUserChatsUseCase {

    override suspend fun invoke(userId: String): Result<List<InterlocutorUserChat>> =
        repository.getUserChats(userId)
}
