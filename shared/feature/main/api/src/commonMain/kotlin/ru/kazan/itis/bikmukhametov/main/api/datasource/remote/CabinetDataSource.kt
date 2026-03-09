package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel

interface CabinetDataSource {
    suspend fun getCabinet(): Result<CabinetModel>
}