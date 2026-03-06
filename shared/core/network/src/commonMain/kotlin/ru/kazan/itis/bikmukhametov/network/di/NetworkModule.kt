package ru.kazan.itis.bikmukhametov.network.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.network.BuildKonfig

/* модуль для сети */
val networkModule = module {

    // хранение куков
    single<CookiesStorage> { AcceptAllCookiesStorage() }

    single {
        HttpClient {

            install(HttpCookies) {
                storage = get()
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            install(WebSockets)

            // Базовый URL
            defaultRequest {
                url(BuildKonfig.BASE_URL)

                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header("Accept", "application/json")

                // Добавляем заголовки из вашего описания
                // В идеале значения 'selectedCabinet' и 'selectedProject'
                // должны извлекаться из вашего UserPreferences/Settings
                //header("X-SPro-Cabinet", get<UserPreferences>().selectedCabinet)
                //header("X-SPro-Project", get<UserPreferences>().selectedProject)
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        // Логика очистки стораджа и переход на экран логина
                        // get<AuthManager>().logout()
                    }
                }
            }

        }
    }
}