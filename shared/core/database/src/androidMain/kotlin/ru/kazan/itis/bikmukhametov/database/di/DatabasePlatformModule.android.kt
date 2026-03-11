package ru.kazan.itis.bikmukhametov.database.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.database.cookie.CookiePersistence
import ru.kazan.itis.bikmukhametov.database.cookie.DataStoreCookiePersistence

private const val COOKIES_DATASTORE_NAME = "cookies"

actual fun databasePlatformModules(): List<Module> = listOf(
    module {
        single<DataStore<Preferences>>(named(COOKIES_DATASTORE_NAME)) {
            val context = get<Context>()
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("cookies.pb") }
            )
        }
        single<CookiePersistence> {
            DataStoreCookiePersistence(get(named(COOKIES_DATASTORE_NAME)))
        }
    }
)
