package ru.kazan.itis.bikmukhametov.profile.impl.data.mapper

import ru.kazan.itis.bikmukhametov.network.auth.model.AuthInfoModel
import ru.kazan.itis.bikmukhametov.profile.api.model.ProfileModel

internal fun AuthInfoModel.toProfileModel(): ProfileModel = ProfileModel(
    id = manager.id,
    email = manager.email,
    name = manager.name,
)
