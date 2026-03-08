package ru.kazan.itis.bikmukhametov.main.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.ChatListTab
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ChatListTabs(
    selectedTab: ChatListTab,
    onTabSelect: (ChatListTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TabChip(
            text = "Все обращения",
            selected = selectedTab == ChatListTab.ALL,
            modifier = Modifier.weight(1f),
            onClick = { onTabSelect(ChatListTab.ALL) }
        )
        TabChip(
            text = "Ждут ответа",
            selected = selectedTab == ChatListTab.WAITING,
            modifier = Modifier.weight(1f),
            onClick = { onTabSelect(ChatListTab.WAITING) }
        )
    }
}

@Composable
private fun TabChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.paddingSmall, horizontal = Spacing.paddingMedium),
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant
    )
}
