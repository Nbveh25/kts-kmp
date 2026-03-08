package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Тело запроса логина (бэкенд принимает login + password). */
@Serializable
internal data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("captcha_token") val captchaToken: String
)

/** Ответ с ошибкой (формат из OpenAPI: code, status, message). */
@Serializable
internal data class ErrorBody(
    @SerialName("code") val code: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null
)
