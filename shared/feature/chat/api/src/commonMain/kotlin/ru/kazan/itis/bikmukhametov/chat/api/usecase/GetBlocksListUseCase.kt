package ru.kazan.itis.bikmukhametov.chat.api.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult

interface GetBlocksListUseCase {
    suspend operator fun invoke(scenarioId: String): Result<BlocksListResult>
}
