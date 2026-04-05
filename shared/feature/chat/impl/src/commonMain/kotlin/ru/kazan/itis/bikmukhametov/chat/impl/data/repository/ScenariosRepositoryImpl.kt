package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import ru.kazan.itis.bikmukhametov.chat.api.datasource.ScenariosDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult
import ru.kazan.itis.bikmukhametov.chat.api.repository.ScenariosRepository

internal class ScenariosRepositoryImpl(
    private val dataSource: ScenariosDataSource,
) : ScenariosRepository {

    override suspend fun getScenariosList(
        kind: String,
        limit: Int,
        offset: Int,
    ): Result<ScenariosListResult> = dataSource.getScenariosList(kind, limit, offset)
}
