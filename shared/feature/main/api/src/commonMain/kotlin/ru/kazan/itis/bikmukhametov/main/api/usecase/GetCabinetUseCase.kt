package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel

interface GetCabinetUseCase {
    suspend operator fun invoke(): Result<List<CabinetModel>>
}
