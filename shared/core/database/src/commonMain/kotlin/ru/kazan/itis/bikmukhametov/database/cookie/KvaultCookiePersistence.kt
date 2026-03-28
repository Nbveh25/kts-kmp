package ru.kazan.itis.bikmukhametov.database.cookie

import com.liftric.kvault.KVault

/** Реализация CookiePersistence через шифрованное хранилище KVault. */
internal class KvaultCookiePersistence(
    private val vault: KVault,
) : CookiePersistence {

    override suspend fun getCookieHeader(): String? =
        vault.string(forKey = COOKIE_HEADER_KEY)

    override suspend fun setCookieHeader(value: String?) {
        if (value == null) {
            vault.clear()
        } else {
            vault.set(key = COOKIE_HEADER_KEY, stringValue = value)
        }
    }

    override suspend fun clear() {
        vault.clear()
    }

    private companion object {
        const val COOKIE_HEADER_KEY = "cookie_header"
    }
}
