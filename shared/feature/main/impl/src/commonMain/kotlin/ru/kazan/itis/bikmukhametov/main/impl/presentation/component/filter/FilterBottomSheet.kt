@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kazan.itis.bikmukhametov.main.impl.presentation.component.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_all_channel_kinds
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_all_channels
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_all_users
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_apply
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_cancel_selection
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_channel
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_channel_type
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_no_buckets
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_no_channels
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.nothing_was_found
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_select_all
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_selected_n
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.search
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_title
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_user_lists
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_generic_chat_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_jivo_chat_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_max_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_telegram_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_viber_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_vk_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_wazzup_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_widget_logo
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ConversationCardItem
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun FilterBottomSheet(
    allChats: List<ConversationCardItem>,
    userListOption: UserListModel?,
    draftKinds: Set<ChannelKind>,
    draftChannelIds: Set<String>,
    draftBuckets: Set<String>,
    onDraftKindsChange: (Set<ChannelKind>) -> Unit,
    onDraftChannelsChange: (Set<String>) -> Unit,
    onDraftBucketsChange: (Set<String>) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val kindOptions = remember { ChannelKind.entries.sortedBy { it.displayName } }
    var kindSearchQuery by remember { mutableStateOf("") }
    val filteredKindOptions = remember(kindSearchQuery, kindOptions) {
        if (kindSearchQuery.isBlank()) kindOptions
        else kindOptions.filter { it.displayName.contains(kindSearchQuery, ignoreCase = true) }
    }
    val channelOptions = remember(allChats) {
        allChats
            .distinctBy { it.channelId }
            .sortedBy { it.channelName ?: it.channelId }
            .map { ChannelPick(id = it.channelId, label = it.channelName ?: it.channelId) }
    }
    var channelSearchQuery by remember { mutableStateOf("") }
    val filteredChannelOptions = remember(channelSearchQuery, channelOptions) {
        if (channelSearchQuery.isBlank()) channelOptions
        else channelOptions.filter { it.label.contains(channelSearchQuery, ignoreCase = true) }
    }
    val allChannelIds = remember(channelOptions) { channelOptions.map { it.id }.toSet() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.horizontalScreenPadding)
                .padding(bottom = Spacing.paddingLarge)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.paddingMedium),
        ) {
            Text(
                text = stringResource(Res.string.filter_title),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            FilterAccordionSection(
                label = stringResource(Res.string.filter_channel_type),
                summary = kindSummary(draftKinds, kindOptions.size),
                enabled = true,
                searchEnabled = true,
                searchQuery = kindSearchQuery,
                searchPlaceholder = "Выберите тип канала",
                onSearchQueryChange = { kindSearchQuery = it },
                onExpandedChange = { expanded ->
                    if (!expanded) kindSearchQuery = ""
                },
            ) {
                val allKindsSelected = draftKinds.size == kindOptions.size
                Text(
                    text = if (allKindsSelected) stringResource(Res.string.filter_cancel_selection)
                    else stringResource(Res.string.filter_select_all),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDraftKindsChange(
                                if (allKindsSelected) emptySet() else kindOptions.toSet(),
                            )
                        }
                        .padding(vertical = Spacing.paddingSmall),
                )
                if (filteredKindOptions.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.nothing_was_found),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                filteredKindOptions.forEach { kind ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = kind in draftKinds,
                            onCheckedChange = {
                                val nextSelected =
                                    if (kind in draftKinds) draftKinds - kind else draftKinds + kind
                                onDraftKindsChange(nextSelected)
                            },
                        )
                        Image(
                            imageVector = vectorResource(kindIcon(kind)),
                            contentDescription = kind.displayName,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(Spacing.paddingSmall))
                        Text(
                            text = kind.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            FilterAccordionSection(
                label = stringResource(Res.string.filter_channel),
                summary = channelSummary(draftChannelIds, channelOptions),
                enabled = channelOptions.isNotEmpty(),
                searchEnabled = true,
                searchQuery = channelSearchQuery,
                searchPlaceholder = "Выберите канал",
                onSearchQueryChange = { channelSearchQuery = it },
                onExpandedChange = { expanded ->
                    if (!expanded) channelSearchQuery = ""
                },
            ) {
                if (channelOptions.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.filter_no_channels),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    val allChannelsSelected = draftChannelIds.size == channelOptions.size
                    Text(
                        text = if (allChannelsSelected) stringResource(Res.string.filter_cancel_selection)
                        else stringResource(Res.string.filter_select_all),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDraftChannelsChange(
                                    if (allChannelsSelected) emptySet() else allChannelIds,
                                )
                            }
                            .padding(vertical = Spacing.paddingSmall),
                    )
                    if (filteredChannelOptions.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.nothing_was_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    filteredChannelOptions.forEach { ch ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = ch.id in draftChannelIds,
                                onCheckedChange = {
                                    val nextSelected =
                                        if (ch.id in draftChannelIds) draftChannelIds - ch.id
                                        else draftChannelIds + ch.id
                                    onDraftChannelsChange(nextSelected)
                                },
                            )
                            Text(
                                text = ch.label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            FilterAccordionSection(
                label = stringResource(Res.string.filter_user_lists),
                summary = bucketSummary(draftBuckets),
                enabled = userListOption != null,
            ) {
                if (userListOption == null) {
                    Text(
                        text = stringResource(Res.string.filter_no_buckets),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = draftBuckets.isEmpty(),
                            onCheckedChange = { checked ->
                                if (checked) onDraftBucketsChange(emptySet())
                            },
                        )
                        Text(
                            text = stringResource(Res.string.filter_all_users),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = userListOption.tag in draftBuckets,
                            onCheckedChange = { checked ->
                                onDraftBucketsChange(
                                    if (checked) setOf(userListOption.tag) else emptySet(),
                                )
                            },
                        )
                        Text(
                            text = userListOption.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.paddingSmall))

            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(
                    text = stringResource(Res.string.filter_apply),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun kindSummary(draft: Set<ChannelKind>, totalKinds: Int): String {
    val all = stringResource(Res.string.filter_all_channel_kinds)
    if (draft.size == totalKinds) return all
    return stringResource(Res.string.filter_selected_n, draft.size)
}

@Composable
private fun channelSummary(draftIds: Set<String>, channels: List<ChannelPick>): String {
    if (channels.isEmpty()) return stringResource(Res.string.filter_no_channels)
    val all = stringResource(Res.string.filter_all_channels)
    if (draftIds.size == channels.size) return all
    return stringResource(Res.string.filter_selected_n, draftIds.size)
}

@Composable
private fun bucketSummary(draft: Set<String>): String {
    val all = stringResource(Res.string.filter_all_users)
    if (draft.isEmpty()) return all
    return stringResource(Res.string.filter_selected_n, draft.size)
}

private fun kindIcon(kind: ChannelKind): DrawableResource = when (kind) {
    ChannelKind.JIVO -> Res.drawable.ic_jivo_chat_logo
    ChannelKind.MAX -> Res.drawable.ic_max_logo
    ChannelKind.TG -> Res.drawable.ic_telegram_logo
    ChannelKind.VB -> Res.drawable.ic_viber_logo
    ChannelKind.WZ -> Res.drawable.ic_wazzup_logo
    ChannelKind.WIDGET -> Res.drawable.ic_widget_logo
    ChannelKind.VK -> Res.drawable.ic_vk_logo
    else -> Res.drawable.ic_generic_chat_logo
}
