package ru.kazan.itis.bikmukhametov.chat.api.repository

import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult

interface ScenariosRepository {
    suspend fun getScenariosList(
        kind: String,
        limit: Int,
        offset: Int,
    ): Result<ScenariosListResult>
}
