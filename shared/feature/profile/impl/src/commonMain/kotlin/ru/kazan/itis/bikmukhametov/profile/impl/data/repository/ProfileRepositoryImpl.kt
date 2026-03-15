package ru.kazan.itis.bikmukhametov.profile.impl.data.repository

import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.network.auth.datasource.AuthDataSource
import ru.kazan.itis.bikmukhametov.network.auth.logout.LogoutService
import ru.kazan.itis.bikmukhametov.network.auth.model.AuthInfoModel
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable
import ru.kazan.itis.bikmukhametov.profile.api.model.ProfileModel
import ru.kazan.itis.bikmukhametov.profile.api.repository.ProfileRepository
import ru.kazan.itis.bikmukhametov.profile.impl.data.mapper.toProfileModel

internal class ProfileRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val logoutService: LogoutService
) : ProfileRepository {

    override suspend fun getProfile(): Result<ProfileModel> {
        return authDataSource.fetchAuthInfo()
            .map { authInfo ->
                authInfo.toProfileModel()
            }
            .onFailure { error ->
                Napier.e(tag = "ProfileRepo", throwable = error) { "Не удалось преобразовать профиль" }
            }
            .mapApiError("Ошибка загрузки профиля")
    }

    override suspend fun logout() = logoutService.logout()

}
