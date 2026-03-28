package ru.kazan.itis.bikmukhametov.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Платформенный модуль сети: только DataStore для space (cookies в core:database).
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
    }
)
