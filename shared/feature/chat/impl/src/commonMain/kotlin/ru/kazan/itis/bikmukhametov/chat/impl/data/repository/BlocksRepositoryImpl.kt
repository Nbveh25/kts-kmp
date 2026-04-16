package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import ru.kazan.itis.bikmukhametov.chat.api.datasource.BlocksDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult
import ru.kazan.itis.bikmukhametov.chat.api.repository.BlocksRepository

internal class BlocksRepositoryImpl(
    private val dataSource: BlocksDataSource,
) : BlocksRepository {

    override suspend fun getBlocksList(scenarioId: String): Result<BlocksListResult> =
        dataSource.getBlocksList(scenarioId)
}
