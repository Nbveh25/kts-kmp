package ru.kazan.itis.bikmukhametov.chat.api.repository

import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult

interface BlocksRepository {
    suspend fun getBlocksList(scenarioId: String): Result<BlocksListResult>
}
