package ru.kazan.itis.bikmukhametov.chat.api.datasource

import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult

interface BlocksDataSource {
    suspend fun getBlocksList(scenarioId: String): Result<BlocksListResult>
}
