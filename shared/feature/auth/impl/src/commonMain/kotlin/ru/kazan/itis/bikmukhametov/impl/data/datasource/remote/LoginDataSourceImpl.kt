package ru.kazan.itis.bikmukhametov.impl.data.datasource.remote

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.HttpClient
import io.ktor.client.statement.request
import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.api.datasource.remote.LoginDataSource
import ru.kazan.itis.bikmukhametov.auth.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

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
        val rawResult = runCatchingCancelable {
            val response = httpClient.post(BuildKonfig.AUTH_BASE_URL + "/api/auth/login") {

                setBody(
                    LoginRequest(
                        email = email,
                        password = password,
                        captchaToken = captchaToken
                    )
                )
            }

            Napier.d(tag = "Login") {
                "POST ${response.request.url} Status: ${response.status}, Headers: ${response.headers.entries()}}"
            }

            // Set-Cookie обработает HttpCookies + PersistentCookieStorage
        }

        return rawResult
            .map { Unit }
            .mapApiError("Ошибка авторизации")
    }
}

