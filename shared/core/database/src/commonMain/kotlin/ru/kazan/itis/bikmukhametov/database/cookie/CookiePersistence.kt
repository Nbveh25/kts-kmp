package ru.kazan.itis.bikmukhametov.database.cookie

/**
 * Контракт хранилища Cookie.
 * Реализация сидит в модуле core:database, а сеть (core:network) использует только этот интерфейс.
 */
interface CookiePersistence {
    /** Возвращает сохранённую строку заголовка Cookie. */
    suspend fun getCookieHeader(): String?

    /** Сохраняет строку заголовка Cookie из ответа Set-Cookie. */
    suspend fun setCookieHeader(value: String?)

    /** Очищает куки (при логауте или 401). */
    suspend fun clear()
}

