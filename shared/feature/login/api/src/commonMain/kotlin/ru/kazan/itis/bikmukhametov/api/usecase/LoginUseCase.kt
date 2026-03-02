package ru.kazan.itis.bikmukhametov.api.usecase

interface LoginUseCase {
    suspend operator fun invoke(username: String, password: String): Result<Unit>
}