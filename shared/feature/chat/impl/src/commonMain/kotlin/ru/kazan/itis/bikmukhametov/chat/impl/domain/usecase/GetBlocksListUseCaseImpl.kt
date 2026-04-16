package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult
import ru.kazan.itis.bikmukhametov.chat.api.repository.BlocksRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetBlocksListUseCase

internal class GetBlocksListUseCaseImpl(
    private val repository: BlocksRepository,
) : GetBlocksListUseCase {

    override suspend fun invoke(scenarioId: String): Result<BlocksListResult> =
        repository.getBlocksList(scenarioId)
}
