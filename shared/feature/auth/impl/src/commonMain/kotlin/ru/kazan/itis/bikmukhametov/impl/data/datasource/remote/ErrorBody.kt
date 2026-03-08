package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/* Ответ с ошибкой */
@Serializable
internal data class ErrorBody(
    @SerialName("code") val code: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null
)
