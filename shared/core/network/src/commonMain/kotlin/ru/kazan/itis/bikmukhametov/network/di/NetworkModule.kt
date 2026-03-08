package ru.kazan.itis.bikmukhametov.network.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.network.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider
import ru.kazan.itis.bikmukhametov.network.auth.LogoutEventBus
import ru.kazan.itis.bikmukhametov.network.cookie.api.CookiePersistence
import ru.kazan.itis.bikmukhametov.network.cookie.impl.PersistentCookieStorage
import ru.kazan.itis.bikmukhametov.network.space.impl.SpaceProviderImpl

/* модуль для сети */
val networkModule = module {

    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    single { LogoutEventBus() }

    // провайдер пространств их топаппбара
    single<SpaceProvider> { SpaceProviderImpl(get(named(PlatformDataStoreNames.SPACE)), get()) }

    // хранение куков
    single<CookiesStorage> { PersistentCookieStorage(get()) }

    single {

        val spaceProvider = get<SpaceProvider>()
        val cookiePersistence = get<CookiePersistence>()
        val logoutBus = get<LogoutEventBus>()
        val appScope = get<CoroutineScope>()

        HttpClient {

            install(HttpCookies) { storage = get() }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    encodeDefaults = true
                })
            }
            
            install(Logging) { level = LogLevel.ALL }
            install(WebSockets)

            // Базовый URL
            defaultRequest {
                url(BuildKonfig.BASE_URL)

                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header("Accept", "application/json")

                // X-SPro-Cabinet, X-SPro-Project — из выбранного пространства (Topbar)
                spaceProvider.cabinet.value?.let { header("X-SPro-Cabinet", it) }
                spaceProvider.project.value?.let { header("X-SPro-Project", it) }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        val path = response.call.request.url.encodedPath
                        // Не сбрасывать сессию при 401 на самом запросе логина (неверные данные)
                        if (!path.contains("auth") && !path.contains("login")) {
                            appScope.launch {
                                cookiePersistence.clear()
                                logoutBus.trigger()
                            }
                        }
                    }
                }
            }

        }
    }
}
