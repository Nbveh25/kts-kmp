package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.ObserveChatUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.SendMessageUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StartBotUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StopBotUseCase
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel

internal class ChatViewModel(
    private val conversationId: String,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getConversationByIdUseCase: GetConversationByIdUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val startBotUseCase: StartBotUseCase,
    private val stopBotUseCase: StopBotUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
) : BaseViewModel<ChatUiState, ChatAction>(ChatUiState()) {

    private var isPageLoading = false
    private var isEndReached = false
    private var hasReceivedBotStateFromWebSocket = false

    init {
        Napier.d { "init conversationId=$conversationId" }
        loadInitialData()
        observeWebSocket()
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

            is ChatAction.OnSendMessageClick -> sendMessage()

            is ChatAction.OnBotToggleClick -> toggleBot()

            is ChatAction.OnMenuExpandChange -> {
                updateState {
                    copy(menuExpanded = action.expanded)
                }
            }

        }
    }

    private fun loadInitialData() {
        loadMessages(reset = true)
        loadConversationInfo()
    }

    private fun observeWebSocket() {
        Napier.w(tag = TAG_VM, message = "▶ observeWebSocket START conversationId=$conversationId")
        viewModelScope.launch {
            observeChatUseCase(conversationId)
                .catch { e ->
                    Napier.e(tag = TAG_VM, message = "▶ observeWebSocket EXCEPTION", throwable = e)
                }
                .collect { newMessage ->
                    Napier.w(
                        tag = TAG_VM,
                        message = "▶ observeWebSocket GOT MESSAGE id=${newMessage.id} text=${
                            newMessage.text.take(60)
                        }"
                    )
                    val botRunningUpdate = when {
                        newMessage.senderType == SenderType.SERVICE && newMessage.text == "start_bot" -> true
                        newMessage.senderType == SenderType.SERVICE && newMessage.text == "stop_bot" -> false
                        else -> null
                    }
                    if (botRunningUpdate != null) hasReceivedBotStateFromWebSocket = true
                    updateState {
                        if (messageList.any { it.id == newMessage.id }) {
                            Napier.w(
                                tag = TAG_VM,
                                message = "▶ duplicate id=${newMessage.id} — skip"
                            )
                            return@updateState if (botRunningUpdate != null) copy(botRunning = botRunningUpdate) else this
                        }
                        copy(
                            messageList = messageList + listOf(newMessage),
                            botRunning = botRunningUpdate ?: botRunning
                        )
                    }
                }
            Napier.w(
                tag = TAG_VM,
                message = "▶ observeWebSocket FLOW COMPLETED (no more emissions)"
            )
        }
    }

    private fun loadConversationInfo() {
        if (conversationId.isBlank()) return
        val requestedConversationId = conversationId
        Napier.d(tag = TAG_VM) {
            "▶ loadConversationInfo START requestedConvId=$requestedConversationId"
        }
        viewModelScope.launch {
            getConversationByIdUseCase(requestedConversationId)
                .onSuccess { conversation ->
                    val returnedId = conversation.id.toString()
                    Napier.d(tag = TAG_VM) {
                        "▶ loadConversationInfo SUCCESS convId=$returnedId userId=${conversation.user.id} fullName=${conversation.user.fullName} avatarUrl=${
                            conversation.user.avatarUrl?.take(50)
                        }"
                    }
                    val requestedLong = requestedConversationId.toLongOrNull()
                    val sameConversation = returnedId == requestedConversationId ||
                        (requestedLong != null && requestedLong == conversation.id)
                    if (!sameConversation) {
                        Napier.w(
                            tag = TAG_VM,
                            message = "▶ loadConversationInfo MISMATCH requested=$requestedConversationId gotConvId=$returnedId — skip UI update",
                        )
                        return@onSuccess
                    }
                    updateState {
                        val apiBotRunning = !conversation.state.isStoppedByManager
                        copy(
                            interlocutorName = conversation.user.fullName,
                            interlocutorAvatarUrl = conversation.user.avatarUrl,
                            botRunning = if (hasReceivedBotStateFromWebSocket) botRunning else apiBotRunning,
                        )
                    }
                }
                .onFailure { e ->
                    Napier.e(
                        tag = TAG_VM,
                        message = "▶ loadConversationInfo FAILED requestedConvId=$requestedConversationId",
                        throwable = e
                    )
                }
        }
    }

    private fun refresh() {
        updateState { copy(isRefreshing = true, loadError = null) }
        isEndReached = false
        isPageLoading = false
        hasReceivedBotStateFromWebSocket = false
        loadInitialData()
    }

    private fun sendMessage() {
        val text = state.value.messageText.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            updateState { copy(messageText = "") }
            sendMessageUseCase(conversationId, text)
                .onSuccess {
                    // Сообщение придёт по WebSocket и отобразится в списке
                }
                .onFailure { e ->
                    Napier.e(message = "Failed to send message", throwable = e)
                    updateState { copy(messageText = text) }
                }
        }
    }

    private fun toggleBot() {
        val currentlyRunning = state.value.botRunning ?: return
        viewModelScope.launch {
            if (currentlyRunning) {
                stopBotUseCase(conversationId)
                    .onSuccess { updateState { copy(botRunning = false) } }
                    .onFailure { e ->
                        Napier.e(message = "Failed to stop bot", throwable = e)
                    }
            } else {
                startBotUseCase(conversationId)
                    .onSuccess { updateState { copy(botRunning = true) } }
                    .onFailure { e ->
                        Napier.e(message = "Failed to start bot", throwable = e)
                    }
            }
        }
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
                        val merged =
                            if (reset) messageList + newMessages else newMessages + messageList
                        copy(
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false,
                            loadError = null,
                            messageList = merged.distinctBy { it.id }.sortedBy { it.createdAt }
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
        private const val TAG_VM = "ChatViewModel"
    }
}

