package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model

import androidx.compose.runtime.Immutable

/** Отложенное (запланированное) событие пользователя (`get_user_planned_events`). */
@Immutable
data class InterlocutorPlannedEvent(
    val id: String,
    val title: String,
    val scheduledAt: String,
)
