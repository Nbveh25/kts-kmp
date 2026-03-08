package ru.kazan.itis.bikmukhametov.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.api.repository.LoginRepository
import ru.kazan.itis.bikmukhametov.api.usecase.LoginUseCase

internal class LoginUseCaseImpl(
    private val loginRepository: LoginRepository
) : LoginUseCase {

    override suspend fun invoke(
        email: String,
        password: String,
        captchaToken: String
    ): Result<Unit> {
        return loginRepository.login(
            email = email,
            password = password,
            captchaToken = captchaToken
        )
    }

}
