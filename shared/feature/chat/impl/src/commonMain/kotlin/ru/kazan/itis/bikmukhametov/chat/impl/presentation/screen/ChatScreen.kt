package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_interlocutor_name
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_arrow_downward_24
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatInputBar
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatRow
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatTopBar
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.DateDivider
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.MessageBubble
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.toItem
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.util.epochDayOf
import ru.kazan.itis.bikmukhametov.ui.util.formatDateLabel

@Composable
fun ChatScreen(
    conversationId: String,
    onBack: () -> Unit,
    onUserInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ChatViewModel = koinViewModel(
        key = "chat-$conversationId",
        parameters = { parametersOf(conversationId) },
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollDown by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val loadMoreThreshold = 3

    val chatRows: List<ChatRow> = remember(state.messageList) {
        val messages = state.messageList.asReversed()
        buildList {
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
                            label = formatDateLabel(msg.createdAt),
                            epochDay = currentDay
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.maxOfOrNull { it.index } ?: -1
            totalItems > 0 && lastVisible >= totalItems - 1 - loadMoreThreshold
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { viewModel.onAction(ChatAction.ListEndReached) }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            ChatTopBar(
                interlocutorName = state.interlocutorName
                    ?: stringResource(Res.string.chat_interlocutor_name),
                interlocutorAvatarUrl = state.interlocutorAvatarUrl,
                onBack = onBack,
                onUserInfoClick = onUserInfoClick,
                botRunning = state.botRunning,
                onBotToggle = { viewModel.onAction(ChatAction.OnBotToggleClick) },
                menuExpanded = state.menuExpanded,
                onMenuExpandChange = { expanded ->
                    viewModel.onAction(ChatAction.OnMenuExpandChange(expanded))
                },
                onRunScenario = {
                    viewModel.onAction(ChatAction.OnMenuExpandChange(false))
                },
            )
        },
        bottomBar = {
            ChatInputBar(
                messageText = state.messageText,
                onMessageTextChange = { text ->
                    viewModel.onAction(ChatAction.OnMessageTextChange(text))
                },
                onAttachClick = { },
                onSendClick = {
                    if (state.messageText.isNotBlank()) {
                        viewModel.onAction(ChatAction.OnSendMessageClick) // TODO: send message
                    }
                },
            )
        }
    ) { paddingValues ->
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
                items(
                    items = chatRows,
                    key = { row ->
                        when (row) {
                            is ChatRow.Message -> row.model.id
                            is ChatRow.DateHeader -> "header_${row.epochDay}"
                        }
                    }
                ) { row ->
                    when (row) {
                        is ChatRow.Message -> MessageBubble(
                            message = row.model.toItem(),
                            showAvatar = row.showAvatar,
                            interlocutorAvatarUrl = state.interlocutorAvatarUrl,
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
                        contentDescription = "Спуск в конец чата"
                    )
                }
            }
        }
    }
}
