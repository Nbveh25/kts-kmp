package ru.kazan.itis.bikmukhametov.database.di

import androidx.room.Room
import com.liftric.kvault.KVault
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import ru.kazan.itis.bikmukhametov.database.cookie.CookiePersistence
import ru.kazan.itis.bikmukhametov.database.cookie.KvaultCookiePersistence
import ru.kazan.itis.bikmukhametov.database.onboarding.InMemoryOnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.onboarding.OnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.database.room.AppDatabase

private const val DB_NAME = "app_database.db"
private const val COOKIES_KVAULT_SERVICE = "ru.kazan.itis.bikmukhametov.cookies"

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

        single<KVault> {
            KVault(COOKIES_KVAULT_SERVICE, null)
        }
        single<CookiePersistence> {
            KvaultCookiePersistence(get())
        }
        single<OnboardingCompletedRepository> { InMemoryOnboardingCompletedRepository() }
    }
)
