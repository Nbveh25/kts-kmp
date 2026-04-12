package ru.kazan.itis.bikmukhametov.database.locale

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class DataStoreAppLanguageRepository(
    private val dataStore: DataStore<Preferences>,
) : AppLanguageRepository {

    override val language: Flow<AppLanguage> = dataStore.data
        .map { prefs -> AppLanguage.fromStoredTag(prefs[LANGUAGE_KEY]) }
        .distinctUntilChanged()

    override suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language.tag
        }
    }

    private companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }
}
