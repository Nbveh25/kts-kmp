package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

class CabinetRepositoryImpl(
    private val cabinetDataSource: CabinetDataSource,
    private val spaceProvider: SpaceProvider
) : CabinetRepository {

    override suspend fun getCabinet(): Result<CabinetModel> =
        cabinetDataSource.getCabinet()
            .onSuccess { cabinet ->
                // сохраняем id кабинета для последующих запросов (X-SPro-Cabinet)
                val currentProject = spaceProvider.project.value ?: ""
                spaceProvider.setSpace(cabinet.id, currentProject)
            }
}
