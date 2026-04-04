package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    object Onboarding : Route

    @Serializable
    object Login : Route

    @Serializable
    object Main : Route

    @Serializable
    object Profile : Route

    @Serializable
    data class Chat(val conversationId: String) : Route

    @Serializable
    data class InterlocutorInfo(
        val conversationId: String,
        val interlocutorName: String = "",
        /** Значение `channel.kind` из API (`ChannelKind.apiValue` в main). */
        val channelKind: String = "",
        val channelName: String = "",
        /** `chat_id` в get_user_vars — Mongo `_id` канала. */
        val chatId: String = "",
        /** `user_id` в get_user_vars — Mongo `_id` пользователя. */
        val userId: String = "",
    ) : Route
}
