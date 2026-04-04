package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_empty_chats
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_empty_events
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_empty_lists
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_empty_variables
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_tab_chats
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_tab_delayed_events
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_tab_lists
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.interlocutor_info_tab_variables
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.mapper.channelKindFromApiRaw
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component.ChatsTabContent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component.DelayedEventsTabContent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component.InterlocutorInfoTopBar
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component.ListsTabContent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component.VariablesTabContent
import ru.kazan.itis.bikmukhametov.theme.Spacing

/**
 * Информация о собеседнике: иконка мессенджера и имя в шапке, вкладки, на «Переменные» — выбор канала и чата.
 */
@Composable
fun InterlocutorInfoScreen(
    conversationId: String,
    interlocutorName: String,
    channelKind: String,
    channelName: String,
    chatId: String,
    userId: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: InterlocutorInfoViewModel = koinViewModel(
        key = "interlocutor-vars-$chatId-$userId",
        parameters = { parametersOf(Pair(chatId, userId)) },
    )
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val resolvedChannelKind = remember(channelKind) { channelKindFromApiRaw(channelKind) }
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = listOf(
        stringResource(Res.string.interlocutor_info_tab_variables),
        stringResource(Res.string.interlocutor_info_tab_lists),
        stringResource(Res.string.interlocutor_info_tab_chats),
        stringResource(Res.string.interlocutor_info_tab_delayed_events),
    )
    val tabEmptyMessages = listOf(
        stringResource(Res.string.interlocutor_info_empty_variables),
        stringResource(Res.string.interlocutor_info_empty_lists),
        stringResource(Res.string.interlocutor_info_empty_chats),
        stringResource(Res.string.interlocutor_info_empty_events),
    )

    val defaultPickerValue = channelName.ifBlank { "KTS_BOT" }
    var selectedChannel by rememberSaveable { mutableStateOf(defaultPickerValue) }
    var selectedChat by rememberSaveable { mutableStateOf(defaultPickerValue) }
    val dropdownOptions = remember(channelName) {
        buildList {
            add(defaultPickerValue)
            if (defaultPickerValue != "KTS_BOT") add("KTS_BOT")
        }.distinct()
    }

    LaunchedEffect(selectedTabIndex, userId) {
        when (selectedTabIndex) {
            1 -> viewModel.onAction(InterlocutorInfoAction.RefreshUserLists)
            2 -> viewModel.onAction(InterlocutorInfoAction.RefreshUserChats)
            3 -> viewModel.onAction(InterlocutorInfoAction.RefreshUserPlannedEvents)
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            InterlocutorInfoTopBar(
                interlocutorName = interlocutorName,
                channelKind = resolvedChannelKind,
                onClose = onClose,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = Spacing.paddingMedium,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            when (selectedTabIndex) {
                0 -> VariablesTabContent(
                    channelKind = resolvedChannelKind,
                    selectedChannel = selectedChannel,
                    selectedChat = selectedChat,
                    options = dropdownOptions,
                    onChannelSelected = { selectedChannel = it },
                    onChatSelected = { selectedChat = it },
                    userVarsState = uiState.userVars,
                    emptyVariablesMessage = tabEmptyMessages[0],
                    onRetryUserVars = { viewModel.onAction(InterlocutorInfoAction.RefreshUserVars) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                )

                1 -> ListsTabContent(
                    userListsState = uiState.userLists,
                    emptyListsMessage = tabEmptyMessages[1],
                    onRetryUserLists = { viewModel.onAction(InterlocutorInfoAction.RefreshUserLists) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                )

                2 -> ChatsTabContent(
                    userChatsState = uiState.userChats,
                    emptyChatsMessage = tabEmptyMessages[2],
                    onRetryUserChats = { viewModel.onAction(InterlocutorInfoAction.RefreshUserChats) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                )

                3 -> DelayedEventsTabContent(
                    userPlannedEventsState = uiState.userPlannedEvents,
                    emptyEventsMessage = tabEmptyMessages[3],
                    onRetryUserPlannedEvents = {
                        viewModel.onAction(InterlocutorInfoAction.RefreshUserPlannedEvents)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                )
            }
        }
    }
}
