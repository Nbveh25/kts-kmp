package ru.kazan.itis.bikmukhametov.profile.impl.presentation.model

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.profile.api.model.ProfileModel

@Immutable
data class ProfileItem(
    val name: String,
    val email: String,
)

fun ProfileModel.toItem() = ProfileItem(
    name = name,
    email = email.orEmpty()
)
