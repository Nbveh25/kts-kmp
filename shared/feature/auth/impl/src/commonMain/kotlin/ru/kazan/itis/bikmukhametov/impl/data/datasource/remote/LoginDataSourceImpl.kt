package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import ru.kazan.itis.bikmukhametov.api.datasource.remote.LoginDataSource
import ru.kazan.itis.bikmukhametov.auth.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError

/* Авторизация по кукам: бэкенд отдаёт Set-Cookie, Ktor + PersistentCookieStorage сохраняют,
 * в последующие запросы кука подставляется автоматически, при 401 — логаут.
 */
internal class LoginDataSourceImpl(
    private val httpClient: HttpClient
) : LoginDataSource {

    override suspend fun login(
        email: String,
        password: String,
        captchaToken: String
    ): Result<Unit> {
        val rawResult = runCatching {
            val response = httpClient.post(BuildKonfig.AUTH_BASE_URL + "/api/auth/login") {

                contentType(ContentType.Application.Json)

                header(HttpHeaders.Accept, "application/json, text/plain, */*")

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
        }

        return rawResult
            .map { Unit }
            .mapApiError("Ошибка авторизации")
    }
}

