package ru.kazan.itis.bikmukhametov.database.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.database.cookie.CookiePersistence
import ru.kazan.itis.bikmukhametov.database.cookie.IosCookiePersistence

actual fun databasePlatformModules(): List<Module> = listOf(
    module {
        single<CookiePersistence> { IosCookiePersistence() }
    }
)
