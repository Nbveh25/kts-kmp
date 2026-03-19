package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

internal data class ChatUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val loadError: String? = null,
    val messageList: List<ChatMessageModel> = emptyList(),
    val messageText: String = "",
    val botRunning: Boolean? = null,
    val menuExpanded: Boolean = false,
    val interlocutorName: String? = null,
    val interlocutorAvatarUrl: String? = null,
)
