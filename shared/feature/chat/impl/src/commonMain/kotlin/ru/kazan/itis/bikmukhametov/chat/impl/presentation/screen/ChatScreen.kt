package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_as_file
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_close_file_picker
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_interlocutor_name
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_scroll_down
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_arrow_downward_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_close_24
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatInputBar
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.RunScenarioDialog
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.platform.AttachmentPickerSheet
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatRow
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatTopBar
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.DateDivider
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.MessageBubble
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.toItem
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.screen.ErrorScreen
import ru.kazan.itis.bikmukhametov.ui.util.TimeFormatStrings
import ru.kazan.itis.bikmukhametov.ui.util.epochDayOf
import ru.kazan.itis.bikmukhametov.ui.util.formatDateLabel
import ru.kazan.itis.bikmukhametov.ui.util.rememberTimeFormatStrings

private const val LOAD_MORE_THRESHOLD_ITEMS = 3

@Composable
fun ChatScreen(
    conversationId: String,
    onBack: () -> Unit,
    onUserInfoClick: (
        interlocutorName: String,
        channelKind: String,
        channelName: String,
        chatId: String,
        userId: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ChatViewModel = koinViewModel(
        key = "chat-$conversationId",
        parameters = { parametersOf(conversationId) },
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val interlocutorDisplayName = state.interlocutorName?.takeIf { it.isNotBlank() }
        ?: stringResource(Res.string.chat_interlocutor_name)

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ChatSendErrorSnackbarEffect(
        sendError = state.sendError,
        snackbarHostState = snackbarHostState,
        onClearSendError = { viewModel.onAction(ChatAction.OnClearSendError) },
    )

    val showScrollDown by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val timeFormatStrings = rememberTimeFormatStrings()

    val chatRows: List<ChatRow> = remember(state.messageList, timeFormatStrings) {
        buildChatRows(state.messageList, timeFormatStrings)
    }

    ChatNewMessageAutoScrollEffect(
        messageListSize = state.messageList.size,
        hasChatRows = chatRows.isNotEmpty(),
        listState = listState,
        scope = scope,
    )

    ChatListPaginationEffect(
        listState = listState,
        loadMoreThreshold = LOAD_MORE_THRESHOLD_ITEMS,
        onListEndReached = { viewModel.onAction(ChatAction.ListEndReached) },
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        snackbarHost = { ChatScreenSnackbarHost(snackbarHostState) },
        topBar = {
            ChatTopBar(
                interlocutorName = interlocutorDisplayName,
                interlocutorAvatarUrl = state.interlocutorAvatarUrl,
                onBack = onBack,
                onUserInfoClick = {
                    onUserInfoClick(
                        interlocutorDisplayName,
                        state.channelKind.orEmpty(),
                        state.channelName.orEmpty(),
                        state.channelMongoId.orEmpty(),
                        state.userMongoId.orEmpty(),
                    )
                },
                botRunning = state.botRunning,
                onBotToggle = { viewModel.onAction(ChatAction.OnBotToggleClick) },
                menuExpanded = state.menuExpanded,
                onMenuExpandChange = { expanded ->
                    viewModel.onAction(ChatAction.OnMenuExpandChange(expanded))
                },
                onRunScenario = {
                    viewModel.onAction(ChatAction.OnOpenRunScenarioDialog)
                },
            )
        },
        bottomBar = {
            ChatScreenBottomBar(state = state, onAction = viewModel::onAction)
        }
    ) { paddingValues ->
        ChatScreenBody(
            paddingValues = paddingValues,
            state = state,
            chatRows = chatRows,
            listState = listState,
            scope = scope,
            showScrollDown = showScrollDown,
            onRefresh = { viewModel.onAction(ChatAction.Refresh) },
            interlocutorAvatarUrl = state.interlocutorAvatarUrl,
        )
    }

    if (state.runScenarioDialogVisible) {
        RunScenarioDialog(
            step = state.runScenarioStep,
            scenarios = state.scenarios,
            scenariosLoading = state.scenariosLoading,
            scenariosLoadError = state.scenariosLoadError,
            searchQuery = state.runScenarioSearchQuery,
            onSearchQueryChange = { viewModel.onAction(ChatAction.OnRunScenarioSearchChange(it)) },
            selectedScenario = state.runScenarioSelectedScenario,
            blocks = state.blocks,
            blocksLoading = state.blocksLoading,
            blocksLoadError = state.blocksLoadError,
            selectedBlockId = state.runScenarioSelectedBlockId,
            onScenarioClick = { viewModel.onAction(ChatAction.OnRunScenarioScenarioClick(it)) },
            onBackToScenarios = { viewModel.onAction(ChatAction.OnRunScenarioBackToScenarioList) },
            onBlockClick = { viewModel.onAction(ChatAction.OnRunScenarioBlockClick(it)) },
            onDismiss = { viewModel.onAction(ChatAction.OnDismissRunScenarioDialog) },
            onSelectClick = { viewModel.onAction(ChatAction.OnDismissRunScenarioDialog) },
        )
    }
}

private fun buildChatRows(
    messageList: List<ChatMessageModel>,
    timeFormatStrings: TimeFormatStrings,
): List<ChatRow> {
    val messages = messageList.asReversed()
    return buildList {
        messages.forEachIndexed { index, msg ->
            val showAvatar = lastOrNull().let { prev ->
                prev !is ChatRow.Message || prev.model.senderType != msg.senderType
            }
            add(ChatRow.Message(model = msg, showAvatar = showAvatar))

            val currentDay = epochDayOf(msg.createdAt)
            val nextDay = if (index < messages.lastIndex) {
                epochDayOf(messages[index + 1].createdAt)
            } else {
                Long.MIN_VALUE
            }
            if (index == messages.lastIndex || currentDay != nextDay) {
                add(
                    ChatRow.DateHeader(
                        label = formatDateLabel(msg.createdAt, timeFormatStrings),
                        epochDay = currentDay
                    )
                )
            }
        }
    }
}

@Composable
private fun ChatSendErrorSnackbarEffect(
    sendError: String?,
    snackbarHostState: SnackbarHostState,
    onClearSendError: () -> Unit,
) {
    LaunchedEffect(sendError) {
        val error = sendError ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = error,
            duration = SnackbarDuration.Long,
        )
        onClearSendError()
    }
}

@Composable
private fun ChatNewMessageAutoScrollEffect(
    messageListSize: Int,
    hasChatRows: Boolean,
    listState: LazyListState,
    scope: CoroutineScope,
) {
    var prevMessageCount by remember { mutableStateOf(0) }
    LaunchedEffect(messageListSize) {
        if (messageListSize > prevMessageCount && hasChatRows) {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val isAtBottom = firstVisibleIndex <= 2
            if (isAtBottom || prevMessageCount == 0) {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }
        prevMessageCount = messageListSize
    }
}

@Composable
private fun ChatListPaginationEffect(
    listState: LazyListState,
    loadMoreThreshold: Int,
    onListEndReached: () -> Unit,
) {
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.maxOfOrNull { it.index } ?: -1
            totalItems > 0 && lastVisible >= totalItems - 1 - loadMoreThreshold
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { onListEndReached() }
    }
}

@Composable
private fun ChatScreenSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}

@Composable
private fun ChatScreenBottomBar(
    state: ChatUiState,
    onAction: (ChatAction) -> Unit,
) {
    Column {
        AttachmentPickerSheet(
            visible = state.attachmentPickerVisible,
            onDismiss = { onAction(ChatAction.OnAttachmentPickerDismiss) },
            onPicked = { onAction(ChatAction.OnAttachmentPicked(it)) },
        )
        state.pendingAttachment?.let { file ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Spacing.paddingSmall,
                            vertical = Spacing.paddingExtraSmall,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = buildString {
                            append(file.fileName)
                            if (file.sendAsFile) append(stringResource(Res.string.chat_as_file))
                        },
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    IconButton(
                        onClick = { onAction(ChatAction.OnClearPendingAttachment) },
                        enabled = !state.isUploading,
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_close_24),
                            contentDescription = stringResource(Res.string.chat_close_file_picker)
                        )
                    }
                }
            }
        }
        ChatInputBar(
            messageText = state.messageText,
            onMessageTextChange = { text ->
                onAction(ChatAction.OnMessageTextChange(text))
            },
            onAttachClick = { onAction(ChatAction.OnOpenAttachmentPicker) },
            attachEnabled = !state.isUploading,
            sendEnabled = !state.isUploading &&
                (state.messageText.isNotBlank() || state.pendingAttachment != null),
            sendInProgress = state.isUploading && state.pendingAttachment != null,
            onSendClick = {
                onAction(ChatAction.OnSendMessageClick)
            },
        )
    }
}

@Composable
private fun ChatScreenBody(
    paddingValues: PaddingValues,
    state: ChatUiState,
    chatRows: List<ChatRow>,
    listState: LazyListState,
    scope: CoroutineScope,
    showScrollDown: Boolean,
    onRefresh: () -> Unit,
    interlocutorAvatarUrl: String?,
) {
    when {
        state.isLoading && state.messageList.isEmpty() && state.loadError == null -> {
            ChatShimmerScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }

        state.loadError != null && state.messageList.isEmpty() -> {
            ErrorScreen(
                modifier = Modifier.padding(paddingValues),
                errorMessage = state.loadError,
                onRetry = onRefresh
            )
        }

        else -> {
            ChatMessageListSection(
                paddingValues = paddingValues,
                chatRows = chatRows,
                listState = listState,
                scope = scope,
                showScrollDown = showScrollDown,
                interlocutorAvatarUrl = interlocutorAvatarUrl,
            )
        }
    }
}

@Composable
private fun ChatMessageListSection(
    paddingValues: PaddingValues,
    chatRows: List<ChatRow>,
    listState: LazyListState,
    scope: CoroutineScope,
    showScrollDown: Boolean,
    interlocutorAvatarUrl: String?,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(
                horizontal = Spacing.paddingMedium,
                vertical = Spacing.paddingSmall
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.paddingSmall),
            reverseLayout = true,
        ) {
            itemsIndexed(
                items = chatRows,
                key = { index, row ->
                    when (row) {
                        is ChatRow.Message -> row.model.id
                        is ChatRow.DateHeader -> "header_$index"
                    }
                }
            ) { _, row ->
                when (row) {
                    is ChatRow.Message -> MessageBubble(
                        message = row.model.toItem(),
                        showAvatar = row.showAvatar,
                        interlocutorAvatarUrl = interlocutorAvatarUrl,
                    )

                    is ChatRow.DateHeader -> DateDivider(label = row.label)
                }
            }
        }

        if (showScrollDown) {
            IconButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Spacing.paddingSmall)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_arrow_downward_24),
                    contentDescription = stringResource(Res.string.chat_scroll_down)
                )
            }
        }
    }
}
