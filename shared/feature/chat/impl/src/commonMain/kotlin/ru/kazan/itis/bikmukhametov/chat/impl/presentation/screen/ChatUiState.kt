package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment

@Immutable
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
    val channelKind: String? = null,
    val channelName: String? = null,
    /** Mongo `_id` канала для API (`chat_id`). */
    val channelMongoId: String? = null,
    /** Mongo `_id` пользователя для API (`user_id`). */
    val userMongoId: String? = null,

    val attachmentPickerVisible: Boolean = false,
    val pendingAttachment: PickedAttachment? = null,
    val isUploading: Boolean = false,
)
