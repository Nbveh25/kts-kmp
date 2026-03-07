package ru.kazan.itis.bikmukhametov.network.cookie.api

/* контракт кук */
interface CookiePersistence {
    /* Возвращает сохранённую строку заголовка Cookie */
    suspend fun getCookieHeader(): String?

    /* Сохраняет строку заголовка Cookie из ответа Set-Cookie. */
    suspend fun setCookieHeader(value: String?)

    /* Очищает куки (при логауте или 401). */
    suspend fun clear()
}