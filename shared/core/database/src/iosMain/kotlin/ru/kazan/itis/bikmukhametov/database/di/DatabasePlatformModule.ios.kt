package ru.kazan.itis.bikmukhametov.database.di

import androidx.room.Room
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import ru.kazan.itis.bikmukhametov.database.cookie.CookiePersistence
import ru.kazan.itis.bikmukhametov.database.cookie.IosCookiePersistence
import ru.kazan.itis.bikmukhametov.database.onboarding.InMemoryOnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.onboarding.OnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.room.AppDatabase

private const val DB_NAME = "app_database.db"

actual fun databasePlatformModules(): List<Module> = listOf(
    module {
        single<AppDatabase> {
            val dbPath = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = true,
                error = null
            )!!.path + "/$DB_NAME"
            Room.databaseBuilder<AppDatabase>(name = dbPath).build()
        }

        single<CookiePersistence> { IosCookiePersistence() }
        single<OnboardingCompletedRepository> { InMemoryOnboardingCompletedRepository() }
    }
)
