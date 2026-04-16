package ru.kazan.itis.bikmukhametov.chat.api.usecase

import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult

interface GetScenariosListUseCase {
    suspend operator fun invoke(
        kind: String,
        limit: Int,
        offset: Int,
    ): Result<ScenariosListResult>
}
