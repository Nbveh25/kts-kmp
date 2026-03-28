package ru.kazan.itis.bikmukhametov.network.space.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

/* Реализация провайдера простарнства на главном экране */
internal class SpaceProviderImpl(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) : SpaceProvider {

    private val _cabinet = MutableStateFlow<String?>(null)
    override val cabinet: StateFlow<String?> = _cabinet.asStateFlow()

    private val _project = MutableStateFlow<String?>(null)
    override val project: StateFlow<String?> = _project.asStateFlow()

    init {
        scope.launch {
            dataStore.data.map { prefs ->
                prefs[CABINET_KEY] to prefs[PROJECT_KEY]
            }.collect { (c, p) ->
                _cabinet.value = c
                _project.value = p
            }
        }
    }

    override suspend fun setSpace(cabinet: String, project: String) {
        dataStore.edit { prefs ->
            prefs[CABINET_KEY] = cabinet
            prefs[PROJECT_KEY] = project
        }
        _cabinet.value = cabinet
        _project.value = project
    }

    override suspend fun getPersistedProjectId(): String? =
        dataStore.data.map { prefs -> prefs[PROJECT_KEY] }.first()

    override suspend fun getPersistedCabinetId(): String? =
        dataStore.data.map { prefs -> prefs[CABINET_KEY] }.first()

    companion object {
        private val CABINET_KEY = stringPreferencesKey("cabinet")
        private val PROJECT_KEY = stringPreferencesKey("project")
    }
}
