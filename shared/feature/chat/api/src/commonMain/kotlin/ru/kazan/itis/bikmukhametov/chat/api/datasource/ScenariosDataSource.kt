package ru.kazan.itis.bikmukhametov.chat.api.datasource

import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult

interface ScenariosDataSource {
    suspend fun getScenariosList(
        kind: String,
        limit: Int,
        offset: Int,
    ): Result<ScenariosListResult>
}
