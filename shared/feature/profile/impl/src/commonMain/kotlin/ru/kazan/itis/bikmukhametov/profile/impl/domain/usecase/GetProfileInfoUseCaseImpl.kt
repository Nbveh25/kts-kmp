package ru.kazan.itis.bikmukhametov.profile.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.profile.api.model.ProfileModel
import ru.kazan.itis.bikmukhametov.profile.api.repository.ProfileRepository
import ru.kazan.itis.bikmukhametov.profile.api.usecase.GetProfileInfoUseCase

class GetProfileInfoUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetProfileInfoUseCase {
    override suspend fun invoke(): Result<ProfileModel> =
        profileRepository.getProfile()
}