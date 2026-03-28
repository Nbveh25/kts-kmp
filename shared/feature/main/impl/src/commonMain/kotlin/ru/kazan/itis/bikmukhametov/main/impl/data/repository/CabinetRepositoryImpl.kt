package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

class CabinetRepositoryImpl(
    private val cabinetDataSource: CabinetDataSource,
    private val spaceProvider: SpaceProvider,
) : CabinetRepository {

    override suspend fun getCabinets(): Result<List<CabinetModel>> {
        val raw = cabinetDataSource.getCabinetList()
        if (raw.isFailure) {
            return Result.failure(raw.exceptionOrNull() ?: IllegalStateException("getCabinetList failed"))
        }
        val cabinets = movePersistedCabinetFirst(raw.getOrThrow())
        cabinets.firstOrNull()?.let { first ->
            val currentProject = spaceProvider.project.value ?: ""
            spaceProvider.setSpace(first.id, currentProject)
        }
        return Result.success(cabinets)
    }

    /** Кабинет из DataStore — первый в списке (остальные порядок как в API). */
    private suspend fun movePersistedCabinetFirst(cabinets: List<CabinetModel>): List<CabinetModel> {
        val storedId = spaceProvider.getPersistedCabinetId()?.takeIf { it.isNotBlank() }
            ?: return cabinets
        val index = cabinets.indexOfFirst { it.id == storedId }
        if (index <= 0) return cabinets
        return buildList(cabinets.size) {
            add(cabinets[index])
            cabinets.forEachIndexed { i, c -> if (i != index) add(c) }
        }
    }
}
