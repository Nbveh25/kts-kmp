package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class ChatViewModel(
    private val conversationId: String,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getConversationByIdUseCase: GetConversationByIdUseCase,
) : BaseViewModel<ChatUiState, ChatAction>(ChatUiState()) {

    private var isPageLoading = false
    private var isEndReached = false

    init {
        Napier.d(tag = "ChatVM") { "Created for conversationId=$conversationId (instance=${hashCode()})" }
        loadMessages(reset = true)
        loadConversationInfo()
    }

    override fun onCleared() {
        super.onCleared()
        Napier.d(tag = "ChatVM") { "Cleared for conversationId=$conversationId (instance=${hashCode()})" }
    }

    private fun loadConversationInfo() {
        if (conversationId.isBlank()) return
        viewModelScope.launch {
            Napier.d(tag = "ChatVM") { "loadConversationInfo → conversationId=$conversationId" }
            getConversationByIdUseCase(conversationId)
                .onSuccess { conversation ->
                    Napier.d(tag = "ChatVM") { "loadConversationInfo → userId=${conversation.user.id}, name=${conversation.user.fullName}" }
                    updateState {
                        copy(
                            interlocutorName = conversation.user.fullName,
                            interlocutorAvatarUrl = conversation.user.avatarUrl,
                            botRunning = !conversation.state.isStoppedByManager,
                        )
                    }
                }
                .onFailure { e ->
                    Napier.e(tag = "ChatVM", throwable = e) {
                        "loadConversationInfo failed for conversationId=$conversationId"
                    }
                }
        }
    }

    override fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.Refresh -> refresh()

            is ChatAction.ListEndReached -> {
                if (isEndReached || isPageLoading) return
                loadMessages()
            }

            is ChatAction.OnMessageTextChange -> {
                updateState { copy(messageText = action.text) }
            }

            is ChatAction.OnSendMessageClick -> {
                // TODO: отправка сообщения на бэкенд (если понадобится)
                // Пока просто чистим поле ввода
                updateState { copy(messageText = "") }
            }

            is ChatAction.OnBotToggleClick -> {
                updateState {
                    copy(botRunning = !botRunning)
                }
            }

            is ChatAction.OnMenuExpandChange -> {
                updateState {
                    copy(menuExpanded = action.expanded)
                }
            }

        }
    }

    private fun refresh() {
        updateState {
            copy(
                isRefreshing = true,
                loadError = null
            )
        }
        isEndReached = false
        isPageLoading = false
        loadMessages(reset = true)
        loadConversationInfo()
    }

    private fun loadMessages(reset: Boolean = false) {
        if (isPageLoading) return

        viewModelScope.launch {
            isPageLoading = true

            val cursor = if (reset) null else state.value.messageList.firstOrNull()

            updateState {
                if (cursor == null) copy(isLoading = true, loadError = null)
                else copy(isLoadingMore = true, loadError = null)
            }

            getChatMessagesUseCase(
                conversationId = conversationId,
                limit = PAGE_SIZE,
                fromId = cursor?.id,
                fromDate = cursor?.createdAt,
            )
                .onSuccess { newMessages ->
                    updateState {
                        copy(
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false,
                            loadError = null,
                            messageList = if (reset) {
                                newMessages
                            } else {
                                // distinctBy удаляет дубли на случай перекрытия страниц API.
                                (newMessages + messageList).distinctBy { it.id }
                            }
                        )
                    }

                    if (newMessages.size < PAGE_SIZE) isEndReached = true
                }
                .onFailure { error ->
                    updateState {
                        copy(
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false,
                            loadError = error.message,
                        )
                    }
                }

            isPageLoading = false
        }
    }



    private companion object {
        private const val PAGE_SIZE = 20
    }
}

