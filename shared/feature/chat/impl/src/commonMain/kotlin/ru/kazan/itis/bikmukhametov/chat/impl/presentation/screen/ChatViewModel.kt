package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetBlocksListUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetScenariosListUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.ObserveChatUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.SendMessageUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.UploadChatAttachmentUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StartBotUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StopBotUseCase
import kotlin.random.Random
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatFileAttachment
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.attachmentDownloadUrl
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment
import ru.kazan.itis.bikmukhametov.ui.util.BaseViewModel
import ru.kazan.itis.bikmukhametov.ui.util.currentTimeMillis
import ru.kazan.itis.bikmukhametov.ui.util.epochMillisToIso8601Utc
import ru.kazan.itis.bikmukhametov.network.auth.datasource.AuthDataSource

internal class ChatViewModel(
    private val conversationId: String,
    private val getScenariosListUseCase: GetScenariosListUseCase,
    private val getBlocksListUseCase: GetBlocksListUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getConversationByIdUseCase: GetConversationByIdUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val uploadChatAttachmentUseCase: UploadChatAttachmentUseCase,
    private val startBotUseCase: StartBotUseCase,
    private val stopBotUseCase: StopBotUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
    private val authDataSource: AuthDataSource,
) : BaseViewModel<ChatUiState, ChatAction>(ChatUiState()) {

    private var isPageLoading = false
    private var isEndReached = false
    private var hasReceivedBotStateFromWebSocket = false

    /** Id вложений после upload, пока ждём то же сообщение по WebSocket (для снятия оптимистичной записи). */
    private var pendingOutgoingAttachmentIds: Set<String> = emptySet()

    /** Email менеджера из сессии — для аватара оператора на оптимистичных сообщениях (см. [MessageBubble] BOT + managerEmail). */
    private var cachedManagerEmail: String? = null

    init {
        Napier.d { "init conversationId=$conversationId" }
        loadInitialData()
        observeWebSocket()
        viewModelScope.launch {
            authDataSource.fetchAuthInfo().onSuccess { auth ->
                cachedManagerEmail = auth.manager.email.takeIf { it.isNotBlank() }
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

            is ChatAction.OnSendMessageClick -> sendMessage()

            is ChatAction.OnBotToggleClick -> toggleBot()

            is ChatAction.OnMenuExpandChange -> {
                updateState {
                    copy(menuExpanded = action.expanded)
                }
            }

            ChatAction.OnOpenRunScenarioDialog -> {
                updateState {
                    copy(
                        menuExpanded = false,
                        runScenarioDialogVisible = true,
                        scenariosLoading = true,
                        scenariosLoadError = null,
                        runScenarioStep = RunScenarioDialogStep.ChooseScenario,
                        runScenarioSelectedScenario = null,
                        blocks = emptyList(),
                        blocksLoading = false,
                        blocksLoadError = null,
                        runScenarioSelectedBlockId = null,
                    )
                }
                loadScenariosForDialog()
            }

            ChatAction.OnDismissRunScenarioDialog -> updateState {
                copy(
                    runScenarioDialogVisible = false,
                    runScenarioSearchQuery = "",
                    scenarios = emptyList(),
                    scenariosLoading = false,
                    scenariosLoadError = null,
                    runScenarioStep = RunScenarioDialogStep.ChooseScenario,
                    runScenarioSelectedScenario = null,
                    blocks = emptyList(),
                    blocksLoading = false,
                    blocksLoadError = null,
                    runScenarioSelectedBlockId = null,
                )
            }

            is ChatAction.OnRunScenarioSearchChange -> updateState {
                copy(runScenarioSearchQuery = action.query)
            }

            is ChatAction.OnRunScenarioScenarioClick -> {
                updateState {
                    copy(
                        runScenarioStep = RunScenarioDialogStep.ChooseBlock,
                        runScenarioSelectedScenario = action.scenario,
                        blocksLoading = true,
                        blocksLoadError = null,
                        blocks = emptyList(),
                        runScenarioSelectedBlockId = null,
                    )
                }
                loadBlocksForDialog(action.scenario.id)
            }

            ChatAction.OnRunScenarioBackToScenarioList -> updateState {
                copy(
                    runScenarioStep = RunScenarioDialogStep.ChooseScenario,
                    runScenarioSelectedScenario = null,
                    blocks = emptyList(),
                    blocksLoading = false,
                    blocksLoadError = null,
                    runScenarioSelectedBlockId = null,
                )
            }

            is ChatAction.OnRunScenarioBlockClick -> updateState {
                copy(runScenarioSelectedBlockId = action.blockId)
            }

            ChatAction.OnOpenAttachmentPicker -> updateState { copy(attachmentPickerVisible = true) }

            ChatAction.OnAttachmentPickerDismiss -> updateState { copy(attachmentPickerVisible = false) }

            is ChatAction.OnAttachmentPicked -> updateState {
                copy(
                    pendingAttachment = action.attachment,
                    attachmentPickerVisible = false,
                )
            }

            ChatAction.OnClearPendingAttachment -> updateState { copy(pendingAttachment = null) }
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
                            newMessage.text
                        }"
                    )
                    val matchedPendingIds = pendingOutgoingAttachmentIds.filter {
                        newMessage.referencesAttachmentId(it)
                    }
                    if (matchedPendingIds.isNotEmpty()) {
                        pendingOutgoingAttachmentIds = pendingOutgoingAttachmentIds - matchedPendingIds.toSet()
                    }
                    val botRunningUpdate = when {
                        newMessage.senderType == SenderType.SERVICE && newMessage.text == "start_bot" -> true
                        newMessage.senderType == SenderType.SERVICE && newMessage.text == "stop_bot" -> false
                        else -> null
                    }
                    if (botRunningUpdate != null) hasReceivedBotStateFromWebSocket = true
                    updateState {
                        var list = messageList
                        if (matchedPendingIds.isNotEmpty()) {
                            list = list.filterNot { m ->
                                m.id.startsWith(OPTIMISTIC_MESSAGE_PREFIX) &&
                                    matchedPendingIds.any { id -> m.referencesAttachmentId(id) }
                            }
                        }
                        if (list.any { it.id == newMessage.id }) {
                            Napier.w(
                                tag = TAG_VM,
                                message = "▶ duplicate id=${newMessage.id} — skip"
                            )
                            return@updateState if (botRunningUpdate != null) {
                                copy(botRunning = botRunningUpdate)
                            } else this
                        }
                        copy(
                            messageList = list + listOf(newMessage),
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
                            conversation.user.avatarUrl
                        }"
                    }
                    val requestedLong = requestedConversationId.toLongOrNull()
                    val sameConversation = returnedId == requestedConversationId ||
                        (requestedLong != null && requestedLong == conversation.id)
                    if (!sameConversation) {
                        Napier.w(
                            tag = TAG_VM,
                            message = "▶ loadConversationInfo requested=$requestedConversationId " +
                                    "gotConvId=$returnedId — skip UI update",
                        )
                        return@onSuccess
                    }
                    updateState {
                        val apiBotRunning = !conversation.state.isStoppedByManager
                        copy(
                            interlocutorName = conversation.user.fullName,
                            interlocutorAvatarUrl = conversation.user.avatarUrl,
                            channelKind = conversation.channel.kind,
                            channelName = conversation.channel.name,
                            channelMongoId = conversation.channel.id.takeIf { it.isNotBlank() },
                            userMongoId = conversation.user.id.takeIf { it.isNotBlank() },
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
        pendingOutgoingAttachmentIds = emptySet()
        updateState {
            copy(
                isRefreshing = true,
                loadError = null,
                messageList = messageList.filterNot { it.id.startsWith(OPTIMISTIC_MESSAGE_PREFIX) },
            )
        }
        isEndReached = false
        isPageLoading = false
        hasReceivedBotStateFromWebSocket = false
        loadInitialData()
    }

    private fun sendMessage() {
        val pending = state.value.pendingAttachment
        val text = state.value.messageText.trim()
        if (text.isBlank() && pending == null) return

        if (pending != null && pending.contentLength != null && pending.contentLength > MAX_ATTACHMENT_BYTES) {
            Napier.e(tag = TAG_VM, message = "Attachment too large: ${pending.contentLength} bytes")
            return
        }

        viewModelScope.launch {
            updateState { copy(isUploading = true) }
            val asDoc = pending?.sendAsFile == true
            val attachmentIds: List<String> = pending?.let { p ->
                uploadChatAttachmentUseCase(
                    fileName = p.fileName,
                    mimeType = p.mimeType,
                    contentUri = p.contentUri,
                    contentLength = p.contentLength,
                ).fold(
                    onSuccess = { listOf(it) },
                    onFailure = { e ->
                        Napier.e(message = "Failed to upload attachment", throwable = e)
                        updateState { copy(isUploading = false) }
                        return@launch
                    },
                )
            } ?: emptyList()
            val bodyText = text.takeIf { it.isNotBlank() }
            sendMessageUseCase(
                conversationId = conversationId,
                text = bodyText,
                attachmentIds = attachmentIds,
                sendAttachmentAsDocument = asDoc,
            )
                .onSuccess {
                    val optimistic = if (attachmentIds.isNotEmpty() && pending != null) {
                        if (cachedManagerEmail == null) {
                            cachedManagerEmail =
                                authDataSource.fetchAuthInfo().getOrNull()?.manager?.email?.takeIf { it.isNotBlank() }
                        }
                        pendingOutgoingAttachmentIds = pendingOutgoingAttachmentIds + attachmentIds.toSet()
                        buildOptimisticOutgoingMessage(
                            text = text,
                            pending = pending,
                            attachmentIds = attachmentIds,
                            sendAsDocument = asDoc,
                        )
                    } else {
                        null
                    }
                    updateState {
                        copy(
                            messageText = "",
                            pendingAttachment = null,
                            isUploading = false,
                            messageList = if (optimistic != null) {
                                messageList + listOf(optimistic)
                            } else {
                                messageList
                            },
                        )
                    }
                }
                .onFailure { e ->
                    Napier.e(message = "Failed to send message", throwable = e)
                    updateState { copy(isUploading = false) }
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
                    val matchedFromHistory = pendingOutgoingAttachmentIds.filter { pendingId ->
                        newMessages.any { it.referencesAttachmentId(pendingId) }
                    }
                    if (matchedFromHistory.isNotEmpty()) {
                        pendingOutgoingAttachmentIds =
                            pendingOutgoingAttachmentIds - matchedFromHistory.toSet()
                    }
                    updateState {
                        var list = messageList
                        if (matchedFromHistory.isNotEmpty()) {
                            list = list.filterNot { m ->
                                m.id.startsWith(OPTIMISTIC_MESSAGE_PREFIX) &&
                                    matchedFromHistory.any { id -> m.referencesAttachmentId(id) }
                            }
                        }
                        val merged =
                            if (reset) list + newMessages else newMessages + list
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

    private fun loadScenariosForDialog() {
        viewModelScope.launch {
            getScenariosListUseCase(kind = "common", limit = 20, offset = 0)
                .onSuccess { result ->
                    updateState {
                        copy(
                            scenariosLoading = false,
                            scenarios = result.scenarios,
                            scenariosLoadError = null,
                        )
                    }
                }
                .onFailure { e ->
                    Napier.e(message = "Failed to load scenarios", throwable = e)
                    updateState {
                        copy(
                            scenariosLoading = false,
                            scenariosLoadError = e.message,
                        )
                    }
                }
        }
    }

    private fun buildOptimisticOutgoingMessage(
        text: String,
        pending: PickedAttachment,
        attachmentIds: List<String>,
        sendAsDocument: Boolean,
    ): ChatMessageModel {
        val id = attachmentIds.first()
        val url = attachmentDownloadUrl(id)
        val asImage = !sendAsDocument && pending.isLikelyInlineImage()
        val images = if (asImage) listOf(url) else emptyList()
        val files = if (asImage) {
            emptyList()
        } else {
            listOf(
                ChatFileAttachment(
                    fileName = pending.fileName,
                    sizeBytes = pending.contentLength?.toInt(),
                    openUrl = url,
                ),
            )
        }
        return ChatMessageModel(
            id = "$OPTIMISTIC_MESSAGE_PREFIX${currentTimeMillis()}-${Random.nextLong()}",
            text = text,
            senderType = SenderType.BOT,
            createdAt = epochMillisToIso8601Utc(currentTimeMillis()),
            managerEmail = cachedManagerEmail,
            imageAttachmentUrls = images,
            localImagePreviewUris = if (asImage) listOf(pending.contentUri) else emptyList(),
            fileAttachments = files,
        )
    }

    private fun loadBlocksForDialog(scenarioId: String) {
        viewModelScope.launch {
            getBlocksListUseCase(scenarioId)
                .onSuccess { result ->
                    updateState {
                        copy(
                            blocksLoading = false,
                            blocks = result.blocks,
                            blocksLoadError = null,
                        )
                    }
                }
                .onFailure { e ->
                    Napier.e(message = "Failed to load blocks", throwable = e)
                    updateState {
                        copy(
                            blocksLoading = false,
                            blocksLoadError = e.message,
                        )
                    }
                }
        }
    }

    private companion object {
        private const val PAGE_SIZE = 20
        private const val TAG_VM = "ChatViewModel"
        private const val MAX_ATTACHMENT_BYTES = 25 * 1024 * 1024
        private const val OPTIMISTIC_MESSAGE_PREFIX = "optimistic-"
    }
}

private fun PickedAttachment.isLikelyInlineImage(): Boolean {
    val t = mimeType?.lowercase().orEmpty()
    if (t.startsWith("image/")) return true
    val f = fileName.lowercase()
    return f.endsWith(".jpg") || f.endsWith(".jpeg") || f.endsWith(".png") ||
        f.endsWith(".gif") || f.endsWith(".webp") || f.endsWith(".bmp") ||
        f.endsWith(".heic") || f.endsWith(".heif")
}

private fun ChatMessageModel.referencesAttachmentId(attachmentId: String): Boolean {
    val needle = "/api/attachments/$attachmentId"
    if (imageAttachmentUrls.any { it.contains(needle) }) return true
    if (fileAttachments.any { it.openUrl?.contains(needle) == true }) return true
    return false
}

