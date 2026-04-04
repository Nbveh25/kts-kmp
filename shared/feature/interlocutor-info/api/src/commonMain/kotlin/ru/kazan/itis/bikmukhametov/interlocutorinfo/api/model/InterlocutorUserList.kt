package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model

import androidx.compose.runtime.Immutable

/**
 * Список рассылок пользователя (вкладка Списки).
 */
@Immutable
data class InterlocutorUserList(
    val id: String,
    val name: String,
    val type: String,
)
