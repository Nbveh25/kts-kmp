package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository.InterlocutorRepository
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserPlannedEventsUseCase

internal class GetUserPlannedEventsUseCaseImpl(
    private val repository: InterlocutorRepository,
) : GetUserPlannedEventsUseCase {

    override suspend fun invoke(chatId: String, userId: String): Result<List<InterlocutorPlannedEvent>> =
        repository.getUserPlannedEvents(chatId, userId)
}
