package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model

import androidx.compose.runtime.Immutable

/**
 * Кастомное поле клиента (вкладка «Переменные»).
 */
@Immutable
data class InterlocutorCustomField(
    val key: String,
    val value: String,
)
