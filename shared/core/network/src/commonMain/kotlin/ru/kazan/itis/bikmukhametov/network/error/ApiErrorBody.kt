package ru.kazan.itis.bikmukhametov.network.error

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Стандартное тело ошибки от backend:
 */
@Serializable
data class ApiErrorBody(
    @SerialName("code") val code: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null
)

/**
 * Преобразует Ktor-исключение в человеко-читаемое Exception с сообщением из ApiErrorBody.
 */
suspend fun Throwable.toApiException(
    defaultMessage: String = "Произошла ошибка, попробуйте позже"
): Exception {
    return when (this) {
        is ClientRequestException,
        is ServerResponseException -> {
            val response = this.response
            val message = runCatching { response.body<ApiErrorBody>().message }
                .getOrNull()
                ?: response.status.description
            Exception(message, this)
        }

        else -> Exception(message ?: defaultMessage, this)
    }
}

/**
 * Хелпер для приведения Result к человеку-читаемой ошибке.
 */
suspend fun <T> Result<T>.mapApiError(
    defaultMessage: String = "Произошла ошибка, попробуйте позже"
): Result<T> =
    if (isSuccess) this else {
        val error = exceptionOrNull()
        if (error != null) Result.failure(error.toApiException(defaultMessage)) else this
    }

