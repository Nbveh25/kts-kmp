package ru.kazan.itis.bikmukhametov.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

/**
 * Платформенный модуль сети: DataStore для space + OkHttp движок с явными таймаутами.
 * Context должен быть в графе Koin (androidContext() при initKoin).
 */
actual fun platformModules(): List<Module> = listOf(
    module {
        single<DataStore<Preferences>>(named(PlatformDataStoreNames.SPACE)) {
            val context = get<Context>()
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("space.pb") }
            )
        }

        // OkHttp с явными таймаутами — перекрывает дефолтные 10 с HTTP/2 стрима.
        // Ktor HttpTimeout plugin ставит таймауты на уровне корутин, но Http2Stream.StreamTimeout
        // в OkHttp срабатывает независимо, поэтому нужна явная конфигурация движка.
        single<HttpClientEngine> {
            OkHttp.create {
                config {
                    connectTimeout(30, TimeUnit.SECONDS)
                    readTimeout(120, TimeUnit.SECONDS)
                    writeTimeout(60, TimeUnit.SECONDS)
                    callTimeout(180, TimeUnit.SECONDS)
                }
            }
        }
    }
)
