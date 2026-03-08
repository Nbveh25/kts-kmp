package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import androidx.compose.runtime.Immutable

/** Бейдж соцсети в карточке чата */
enum class SocialBadge {
    TG,
    WA
}

@Immutable
internal data class ChatCardUi(
    val id: String,
    val avatarUrl: String? = null,
    val socialBadge: SocialBadge,
    val name: String,
    val lastMessageText: String,
    val timeOrDate: String,
    val unreadCount: Int = 0
)
