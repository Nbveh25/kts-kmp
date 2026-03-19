package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.usecase.SetProjectUseCase
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

/**
 * Сохраняет выбранный проект (и кабинет) в SpaceProvider.
 * Используется при переключении проекта в topbar на MainScreen.
 */
internal class SetProjectUseCaseImpl(
    private val spaceProvider: SpaceProvider
) : SetProjectUseCase {

    override suspend fun invoke(cabinetId: String, projectId: String): Result<Unit> {
        return runCatchingCancelable {
            spaceProvider.setSpace(cabinet = cabinetId, project = projectId)
        }
    }
}
