package ru.kazan.itis.bikmukhametov.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.api.repository.LoginRepository
import ru.kazan.itis.bikmukhametov.api.usecase.LoginUseCase

internal class LoginUseCaseImpl(
    private val loginRepository: LoginRepository
) : LoginUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): Result<Unit> {
        return loginRepository.login(
            username = username,
            password = password
        )
    }

}
