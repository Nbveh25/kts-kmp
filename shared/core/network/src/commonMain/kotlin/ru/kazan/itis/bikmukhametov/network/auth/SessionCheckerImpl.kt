package ru.kazan.itis.bikmukhametov.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.network.BuildKonfig

// Реализация проверки сессии через эндпоинт /auth/info.
internal class SessionCheckerImpl(
    private val httpClient: HttpClient,
) : SessionChecker {

    override suspend fun isSessionValid(): Boolean {
        val result = runCatching {
            httpClient.get(BuildKonfig.BASE_URL + "/auth/info")
        }

        val response: HttpResponse = result.getOrElse {
            return false
        }

        Napier.d(tag = "SessionChecker") { "REQUEST HEADERS: ${response.call.request.headers.entries()}" }
        Napier.d(tag = "SessionChecker") { "RESPONSE HEADERS: ${response.headers.entries()}" }
        Napier.d(tag = "SessionChecker") { "STATUS: ${response.status}" }


        if (response.status == HttpStatusCode.OK) {
            return true
        }

        // Для 401 у нас уже срабатывает HttpResponseValidator:
        //   - чистит куки
        //   - триггерит LogoutEventBus
        // Здесь просто возвращаем false.
        return false
    }
}

