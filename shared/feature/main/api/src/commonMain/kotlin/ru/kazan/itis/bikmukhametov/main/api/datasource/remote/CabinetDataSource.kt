package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel

/* Cabinet контракт */
interface CabinetDataSource {
    suspend fun getCabinetList(): Result<List<CabinetModel>>
}
