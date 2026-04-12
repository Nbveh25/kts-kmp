package ru.kazan.itis.bikmukhametov.database.locale

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

internal class IosAppLanguageRepository : AppLanguageRepository {

    private val defaults = NSUserDefaults.standardUserDefaults
    private val _language = MutableStateFlow(readFromStorage())

    private fun readFromStorage(): AppLanguage {
        val tag = defaults.stringForKey(KEY) as? String
        return AppLanguage.fromStoredTag(tag)
    }

    override val language: Flow<AppLanguage> = _language.asStateFlow()

    override suspend fun setLanguage(language: AppLanguage) {
        defaults.setObject(language.tag, forKey = KEY)
        _language.value = language
    }

    private companion object {
        const val KEY = "app_language"
    }
}
