package ru.kazan.itis.bikmukhametov.profile.api.repository

import ru.kazan.itis.bikmukhametov.profile.api.model.ProfileModel

interface ProfileRepository {
    suspend fun getProfile(): Result<ProfileModel>
    suspend fun logout()
}
