package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/* Тело запроса логина */
@Serializable
internal data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("captcha_token") val captchaToken: String
)
