package ru.kazan.itis.bikmukhametov.database.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.database.cookie.CookiePersistence
import ru.kazan.itis.bikmukhametov.database.cookie.DataStoreCookiePersistence
import ru.kazan.itis.bikmukhametov.database.onboarding.DataStoreOnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.onboarding.OnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.room.AppDatabase

private const val COOKIES_DATASTORE_NAME = "cookies"
private const val APP_PREFS_DATASTORE_NAME = "app_prefs"
private const val DB_NAME = "app_database.db"

actual fun databasePlatformModules(): List<Module> = listOf(
    module {
        single<AppDatabase> {
            val context = get<Context>()
            Room.databaseBuilder<AppDatabase>(
                context = context,
                name = context.getDatabasePath(DB_NAME).absolutePath
            ).build()
        }

        single<DataStore<Preferences>>(named(COOKIES_DATASTORE_NAME)) {
            val context = get<Context>()
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("cookies.pb") }
            )
        }
        single<CookiePersistence> {
            DataStoreCookiePersistence(get(named(COOKIES_DATASTORE_NAME)))
        }
        single<DataStore<Preferences>>(named(APP_PREFS_DATASTORE_NAME)) {
            val context = get<Context>()
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("app_prefs.pb") }
            )
        }
        single<OnboardingCompletedRepository> {
            DataStoreOnboardingCompletedRepository(get(named(APP_PREFS_DATASTORE_NAME)))
        }
    }
)
