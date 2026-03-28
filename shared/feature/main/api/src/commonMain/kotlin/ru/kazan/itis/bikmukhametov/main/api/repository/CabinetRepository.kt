package ru.kazan.itis.bikmukhametov.main.api.repository

import ru.kazan.itis.bikmukhametov.main.api.model.CabinetModel

interface CabinetRepository {
    /** Список кабинетов; сохранённый из DataStore — первый (пока API отдаёт один — список из одного элемента). */
    suspend fun getCabinets(): Result<List<CabinetModel>>
}
