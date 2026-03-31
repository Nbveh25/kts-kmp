package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.space.CabinetModel

interface GetCabinetUseCase {
    suspend operator fun invoke(): Result<List<CabinetModel>>
}
