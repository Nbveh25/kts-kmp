package ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model

import androidx.compose.runtime.Immutable

/**
 * Чат пользователя в других каналах (вкладка «Чаты», `get_user_chats`).
 */
@Immutable
data class InterlocutorUserChat(
    val id: String,
    val externalId: String,
    val title: String,
    /** Сырое значение `channel_kind` из API (например `tg`). */
    val channelKind: String,
    val channelId: String,
    /** `chat` или `bot` и т.п. */
    val realm: String,
    val isEnabled: Boolean,
)
