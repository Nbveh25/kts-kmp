package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource

class CabinetRepositoryImpl(
    private val remote: CabinetDataSource
) : CabinetRepository {

    override suspend fun getCabinet(): Result<CabinetModel> =
        remote.getCabinet()
}