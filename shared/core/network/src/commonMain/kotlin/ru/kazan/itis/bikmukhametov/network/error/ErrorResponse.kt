package ru.kazan.itis.bikmukhametov.network.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String? = null,
    val code: String? = null
)
