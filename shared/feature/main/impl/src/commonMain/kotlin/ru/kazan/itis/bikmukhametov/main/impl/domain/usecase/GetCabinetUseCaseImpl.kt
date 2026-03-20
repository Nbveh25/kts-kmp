package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository

class GetCabinetUseCaseImpl(
    private val cabinetRepository: CabinetRepository
) : GetCabinetUseCase {

    override suspend fun invoke(): Result<List<CabinetModel>> =
        cabinetRepository.getCabinets()
}
