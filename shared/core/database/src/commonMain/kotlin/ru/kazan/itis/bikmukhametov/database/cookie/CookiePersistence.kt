package ru.kazan.itis.bikmukhametov.database.cookie

// Контракт хранилища Cookie.
interface CookiePersistence {
    suspend fun getCookieHeader(): String?

    suspend fun setCookieHeader(value: String?)

    suspend fun clear()
}

