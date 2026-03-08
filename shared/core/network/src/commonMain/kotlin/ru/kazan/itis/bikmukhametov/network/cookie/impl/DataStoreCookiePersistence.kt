package ru.kazan.itis.bikmukhametov.network.cookie.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kazan.itis.bikmukhametov.network.cookie.api.CookiePersistence

/* Реализация CookiePersistence с DataStore */
class DataStoreCookiePersistence(
    private val dataStore: DataStore<Preferences>
) : CookiePersistence {

    override suspend fun getCookieHeader(): String? =
        dataStore.data.map { it[COOKIE_HEADER_KEY] }.first()

    override suspend fun setCookieHeader(value: String?) {
        dataStore.edit { prefs ->
            if (value == null) prefs.remove(COOKIE_HEADER_KEY)
            else prefs[COOKIE_HEADER_KEY] = value
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.remove(COOKIE_HEADER_KEY) }
    }

    companion object {
        private val COOKIE_HEADER_KEY = stringPreferencesKey("cookie_header")
    }
}
