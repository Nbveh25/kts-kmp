package ru.kazan.itis.bikmukhametov.network.space.api

import kotlinx.coroutines.flow.StateFlow

/*
 * Провайдер выбранного пространства (кабинет + проект).
 * Используется для заголовков X-SPro-Cabinet и X-SPro-Project.
 * StateFlow — для синхронного доступа в defaultRequest (не suspend).
 */
interface SpaceProvider {
    val cabinet: StateFlow<String?>
    val project: StateFlow<String?>

    suspend fun setSpace(cabinet: String, project: String)
}
