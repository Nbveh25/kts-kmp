package ru.kazan.itis.bikmukhametov.main.api.usecase

interface SetProjectUseCase {
    suspend operator fun invoke(cabinetId: String, projectId: String): Result<Unit>
}
