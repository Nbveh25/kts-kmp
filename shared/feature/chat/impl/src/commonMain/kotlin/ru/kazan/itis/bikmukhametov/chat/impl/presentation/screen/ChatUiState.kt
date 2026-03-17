package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

internal data class ChatUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val loadError: String? = null,
    val messageList: List<ChatMessageModel> = emptyList(),
    val messageText: String = "",
    val botRunning: Boolean = true,
    val menuExpanded: Boolean = false,
    /** Имя собеседника из API (get_conversation). Если null — использовать переданное в экран. */
    val interlocutorName: String? = null,
    /** URL аватара собеседника из API. Если null — использовать переданный в экран. */
    val interlocutorAvatarUrl: String? = null,
)
