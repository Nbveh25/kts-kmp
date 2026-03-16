package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class ChatViewModel(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ChatUiState, ChatAction>(ChatUiState()) {

    private val conversationId: String =
        savedStateHandle.get<String>("conversationId") ?: ""

    private var isPageLoading = false
    private var isEndReached = false

    init {
        loadMessages(reset = true)
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
    }

    private fun loadMessages(reset: Boolean = false) {
        if (isPageLoading) return

        viewModelScope.launch {
            isPageLoading = true

            // Курсор — самое старое загруженное сообщение (первый элемент ascending-списка).
            // Для первой загрузки (reset) курсор не передаётся.
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
                                // Старые сообщения идут перед текущими (ascending order),
                                // чтобы asReversed() в UI правильно отрисовал их сверху.
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

