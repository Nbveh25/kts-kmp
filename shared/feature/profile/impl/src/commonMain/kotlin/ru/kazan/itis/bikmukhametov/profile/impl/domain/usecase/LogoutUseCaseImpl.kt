package ru.kazan.itis.bikmukhametov.profile.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.profile.api.repository.ProfileRepository
import ru.kazan.itis.bikmukhametov.profile.api.usecase.LogoutUseCase

class LogoutUseCaseImpl(
    private val profileRepository: ProfileRepository
) : LogoutUseCase {
    override suspend fun invoke() = profileRepository.logout()

}