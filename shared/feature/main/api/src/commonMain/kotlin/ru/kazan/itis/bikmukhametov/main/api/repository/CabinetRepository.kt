package ru.kazan.itis.bikmukhametov.main.api.repository

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel

interface CabinetRepository {
    suspend fun getCabinet(): Result<CabinetModel>
}
