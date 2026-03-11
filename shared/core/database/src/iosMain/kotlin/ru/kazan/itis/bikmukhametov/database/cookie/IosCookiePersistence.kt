package ru.kazan.itis.bikmukhametov.database.cookie

/**
 * Заглушка для iOS. При необходимости можно заменить на Keychain / UserDefaults.
 */
class IosCookiePersistence : CookiePersistence {
    private var header: String? = null

    override suspend fun getCookieHeader(): String? = header

    override suspend fun setCookieHeader(value: String?) {
        header = value
    }

    override suspend fun clear() {
        header = null
    }
}
