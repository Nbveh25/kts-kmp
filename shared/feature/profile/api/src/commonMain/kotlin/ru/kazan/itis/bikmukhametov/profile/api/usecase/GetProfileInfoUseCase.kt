package ru.kazan.itis.bikmukhametov.profile.api.usecase

import ru.kazan.itis.bikmukhametov.profile.api.model.ProfileModel

interface GetProfileInfoUseCase {
    suspend operator fun invoke(): Result<ProfileModel>
}
