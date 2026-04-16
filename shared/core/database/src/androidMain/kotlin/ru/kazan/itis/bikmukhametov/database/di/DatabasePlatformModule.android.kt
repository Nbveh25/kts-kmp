package ru.kazan.itis.bikmukhametov.database.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.liftric.kvault.KVault
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.database.cookie.CookiePersistence
import ru.kazan.itis.bikmukhametov.database.cookie.KvaultCookiePersistence
import ru.kazan.itis.bikmukhametov.database.locale.AppLanguageRepository
import ru.kazan.itis.bikmukhametov.database.locale.DataStoreAppLanguageRepository
import ru.kazan.itis.bikmukhametov.database.onboarding.DataStoreOnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.onboarding.OnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.room.AppDatabase
private const val DB_NAME = "app_database.db"
private const val COOKIES_KVAULT_NAME = "cookies"

actual fun databasePlatformModules(): List<Module> = listOf(
    module {
        single<AppDatabase> {
            val context = get<Context>()
            Room.databaseBuilder<AppDatabase>(
                context = context,
                name = context.getDatabasePath(DB_NAME).absolutePath
            ).build()
        }

        single<KVault> {
            KVault(get<Context>(), COOKIES_KVAULT_NAME)
        }
        single<CookiePersistence> {
            KvaultCookiePersistence(get())
        }
        single<DataStore<Preferences>>(named(AppPreferencesQualifier.NAME)) {
            val context = get<Context>()
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("app_prefs.pb") }
            )
        }
        single<OnboardingCompletedRepository> {
            DataStoreOnboardingCompletedRepository(get(named(AppPreferencesQualifier.NAME)))
        }
        single<AppLanguageRepository> {
            DataStoreAppLanguageRepository(get(named(AppPreferencesQualifier.NAME)))
        }
    }
)
