package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult
import ru.kazan.itis.bikmukhametov.chat.api.repository.ScenariosRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetScenariosListUseCase

internal class GetScenariosListUseCaseImpl(
    private val repository: ScenariosRepository,
) : GetScenariosListUseCase {

    override suspend fun invoke(
        kind: String,
        limit: Int,
        offset: Int,
    ): Result<ScenariosListResult> = repository.getScenariosList(kind, limit, offset)
}
