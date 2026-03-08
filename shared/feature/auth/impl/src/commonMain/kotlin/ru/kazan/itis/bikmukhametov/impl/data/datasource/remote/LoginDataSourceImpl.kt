package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import ru.kazan.itis.bikmukhametov.api.datasource.remote.LoginDataSource

/**
 * Реальный логин через бэкенд.
 * Авторизация по кукам: бэкенд отдаёт Set-Cookie, Ktor + PersistentCookieStorage сохраняют,
 * в последующие запросы кука подставляется автоматически. Рефреш не предусмотрен, при 401 — логаут.
 */
internal class LoginDataSourceImpl(
    private val httpClient: HttpClient
) : LoginDataSource {

    override suspend fun login(
        email: String,
        password: String,
        captchaToken: String
    ): Result<Unit> {
        return runCatching {
            val response = httpClient.post("https://auth.smartbotpro.ru/api/auth/login") {
                contentType(ContentType.Application.Json)

                // Заголовки как в веб-версии (curl auth.smartbotpro.ru)
                header(HttpHeaders.Accept, "application/json, text/plain, */*")
                header(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0"
                )
                header(HttpHeaders.Referrer, "https://auth.smartbotpro.ru/auth/login/")
                header(HttpHeaders.Origin, "https://auth.smartbotpro.ru")

                // Пустые заголовки кабинета/проекта как в веб-форме логина
                header("X-SPro-Cabinet", "")
                header("X-SPro-Project", "")



                setBody(
                    LoginRequest(
                        email = email,
                        password = password,
                        captchaToken = captchaToken
                    )
                )
            }

            println(
                """
            --> HTTP POST ${response.request.url}
            Status: ${response.status}
            Headers: ${response.headers.entries()}
            Body: ${response.bodyAsText()}
            <-- END HTTP
            """.trimIndent()
            )
            // Set-Cookie обработает HttpCookies + PersistentCookieStorage
        }.fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { e ->
                when (e) {
                    is ClientRequestException -> {
                        val msg = runCatching { e.response.body<ErrorBody>().message }.getOrNull()
                            ?: e.response.status.description
                        Result.failure<Unit>(Exception(msg))
                    }

                    else -> Result.failure(e)
                }
            }
        )
    }
}
