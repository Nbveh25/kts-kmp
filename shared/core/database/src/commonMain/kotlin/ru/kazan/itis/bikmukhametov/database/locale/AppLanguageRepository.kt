package ru.kazan.itis.bikmukhametov.database.locale

import kotlinx.coroutines.flow.Flow

interface AppLanguageRepository {
    val language: Flow<AppLanguage>

    suspend fun setLanguage(language: AppLanguage)
}
