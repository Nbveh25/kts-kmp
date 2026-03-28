@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kazan.itis.bikmukhametov.main.impl.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_all_channel_kinds
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_all_channels
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_all_users
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_apply
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_channel
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_channel_type
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_no_buckets
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_no_channels
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_selected_n
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_title
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.filter_user_lists
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ConversationCardItem
import ru.kazan.itis.bikmukhametov.theme.Spacing

private data class ChannelPick(val id: String, val label: String)

@Composable
internal fun FilterBottomSheet(
    allChats: List<ConversationCardItem>,
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
    val channelOptions = remember(allChats) {
        allChats
            .distinctBy { it.channelId }
            .sortedBy { it.channelName ?: it.channelId }
            .map { ChannelPick(id = it.channelId, label = it.channelName ?: it.channelId) }
    }
    val bucketOptions = remember(allChats) {
        allChats.mapNotNull { it.userListBucket }.distinct().sorted()
    }

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
            ) {
                kindOptions.forEach { kind ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = kind in draftKinds,
                            onCheckedChange = {
                                val next = if (kind in draftKinds) draftKinds - kind else draftKinds + kind
                                onDraftKindsChange(next)
                            },
                        )
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
            ) {
                if (channelOptions.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.filter_no_channels),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    channelOptions.forEach { ch ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = ch.id in draftChannelIds,
                                onCheckedChange = {
                                    val next =
                                        if (ch.id in draftChannelIds) draftChannelIds - ch.id
                                        else draftChannelIds + ch.id
                                    onDraftChannelsChange(next)
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
                summary = bucketSummary(draftBuckets, bucketOptions),
                enabled = bucketOptions.isNotEmpty(),
            ) {
                if (bucketOptions.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.filter_no_buckets),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    bucketOptions.forEach { bucket ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = bucket in draftBuckets,
                                onCheckedChange = {
                                    val next =
                                        if (bucket in draftBuckets) draftBuckets - bucket else draftBuckets + bucket
                                    onDraftBucketsChange(next)
                                },
                            )
                            Text(
                                text = bucket,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                            )
                        }
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
    if (draft.isEmpty() || draft.size == totalKinds) return all
    return stringResource(Res.string.filter_selected_n, draft.size)
}

@Composable
private fun channelSummary(draftIds: Set<String>, channels: List<ChannelPick>): String {
    if (channels.isEmpty()) return stringResource(Res.string.filter_no_channels)
    val all = stringResource(Res.string.filter_all_channels)
    if (draftIds.isEmpty() || draftIds.size == channels.size) return all
    return stringResource(Res.string.filter_selected_n, draftIds.size)
}

@Composable
private fun bucketSummary(draft: Set<String>, buckets: List<String>): String {
    val all = stringResource(Res.string.filter_all_users)
    if (buckets.isEmpty()) return all
    if (draft.isEmpty() || draft.size == buckets.size) return all
    return stringResource(Res.string.filter_selected_n, draft.size)
}

@Composable
private fun FilterAccordionSection(
    label: String,
    summary: String,
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val fieldTapInteraction = remember { MutableInteractionSource() }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = summary,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Text(
                        text = "▼",
                        modifier = Modifier.rotate(if (expanded) 180f else 0f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = MaterialTheme.shapes.medium,
            )
            if (enabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = fieldTapInteraction,
                            indication = null,
                        ) { expanded = !expanded },
                )
            }
        }
        AnimatedVisibility(visible = expanded && enabled) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.paddingSmall),
            ) {
                content()
            }
        }
    }
}
