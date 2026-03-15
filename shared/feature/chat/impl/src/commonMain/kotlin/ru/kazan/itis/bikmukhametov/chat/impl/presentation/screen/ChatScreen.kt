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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatInputBar
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.ChatTopBar
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.MessageBubble
import ru.kazan.itis.bikmukhametov.theme.Spacing

/** Тип отправителя сообщения для стилизации */
enum class MessageSender {
    OPERATOR,
    CLIENT,
    SYSTEM
}

/** Модель сообщения для UI (заглушка под реальные данные) */
data class ChatMessageUi(
    val id: String,
    val text: String,
    val sender: MessageSender,
    val imageUrl: String? = null,
)

@Composable
fun ChatScreen(
    conversationId: String,
    interlocutorName: String?,
    interlocutorAvatarUrl: String?,
    onBack: () -> Unit,
    onUserInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var messageText by remember { mutableStateOf("") }
    var botRunning by remember { mutableStateOf(true) }
    var menuExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollDown = listState.firstVisibleItemIndex > 0

    // Заглушка сообщений для вёрстки
    val placeholderMessages = remember {
        listOf(
            ChatMessageUi("1", "Здравствуйте! Чем могу помочь?", MessageSender.OPERATOR),
            ChatMessageUi("2", "Хочу уточнить по заказу №12345", MessageSender.CLIENT),
            ChatMessageUi("3", "Оператор подключён к диалогу", MessageSender.SYSTEM),
            ChatMessageUi("4", "Проверяю информацию по заказу…", MessageSender.OPERATOR),
            ChatMessageUi("5", "Спасибо, буду ждать", MessageSender.CLIENT),
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            ChatTopBar(
                interlocutorName = interlocutorName ?: "Собеседник",
                interlocutorAvatarUrl = interlocutorAvatarUrl,
                onBack = onBack,
                onUserInfoClick = onUserInfoClick,
                botRunning = botRunning,
                onBotToggle = { botRunning = !botRunning },
                menuExpanded = menuExpanded,
                onMenuExpandChange = { menuExpanded = it },
                onRunScenario = { menuExpanded = false },
            )
        },
        bottomBar = {
            ChatInputBar(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onAttachClick = { },
                onSendClick = { if (messageText.isNotBlank()) messageText = "" },
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
            ) {
                items(
                    items = placeholderMessages,
                    key = { it.id }
                ) { msg ->
                    MessageBubble(message = msg)
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
                        .align(Alignment.BottomCenter)
                        .padding(Spacing.paddingMedium)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        )
                ) {
                    Text("↓", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

