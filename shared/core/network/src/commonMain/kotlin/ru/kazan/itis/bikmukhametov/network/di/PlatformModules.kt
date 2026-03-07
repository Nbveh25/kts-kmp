package ru.kazan.itis.bikmukhametov.network.di

import org.koin.core.module.Module

/** Имена qualifier для DataStore (cookies, space). */
object PlatformDataStoreNames {
    const val COOKIES = "cookies"
    const val SPACE = "space"
}

/**
 * Платформенные Koin-модули (DataStore, CookiePersistence и т.д.).
 * На Android возвращает модуль с реальной реализацией, на iOS — пустой список (пока).
 */
expect fun platformModules(): List<Module>
