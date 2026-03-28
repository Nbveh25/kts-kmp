package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.ChatListTabs
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ConversationCardItem
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.ConvesationCardUI
import ru.kazan.itis.bikmukhametov.theme.Spacing

/**
 * Экран отображения списка чатов (успешное состояние)
 *
 * @param selectedTab текущая выбранная вкладка
 * @param onTabSelect callback при смене вкладки
 * @param chats список чатов для отображения
 * @param isLoadingMore флаг загрузки следующей страницы (пагинация)
 * @param isRefreshing флаг активного pull-to-refresh
 * @param onRefresh callback при pull-to-refresh
 * @param onListEndReached callback при достижении конца списка
 * @param onChatClick callback при клике на чат (опционально)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedTab: ChatListTab,
    onTabSelect: (ChatListTab) -> Unit,
    chats: List<ConversationCardItem>,
    isLoadingMore: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onListEndReached: () -> Unit,
    onChatClick: ((ConversationCardItem) -> Unit)? = null,
    listState: LazyListState = rememberLazyListState()
) {
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChatListTabs(
                selectedTab = selectedTab,
                onTabSelect = onTabSelect,
                modifier = Modifier.padding(
                    horizontal = Spacing.paddingMedium,
                    vertical = Spacing.paddingSmall
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                if (chats.isEmpty()) {
                    item {
                        EmptySearchState(modifier = Modifier.fillParentMaxSize())
                    }
                } else {

                    itemsIndexed(
                        items = chats,
                        key = { _, item -> item.id }
                    ) { index, chat ->
                        ConvesationCardUI(
                            chat = chat,
                            modifier = Modifier
                                .padding(
                                    horizontal = Spacing.paddingMedium,
                                    vertical = Spacing.paddingExtraSmall
                                )
                                .let { mod ->
                                    if (onChatClick != null) {
                                        mod.clickable { onChatClick(chat) }
                                    } else {
                                        mod
                                    }
                                }
                        )

                        if (index == chats.lastIndex) {
                            LaunchedEffect(chats.size) {
                                onListEndReached()
                            }
                        }
                    }
                }

                // Индикатор загрузки внизу при пагинации
                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = Spacing.paddingMedium),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
