package ru.kazan.itis.bikmukhametov.api.usecase

interface LoginUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        captchaToken: String
    ): Result<Unit>
}
