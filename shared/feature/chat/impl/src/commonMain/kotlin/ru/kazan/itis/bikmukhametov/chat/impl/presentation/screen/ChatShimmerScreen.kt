package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.component.MessageBubbleShimmer
import ru.kazan.itis.bikmukhametov.theme.Spacing

private val shimmerPattern = listOf(
    true, true, false, true, true, true, false, false, true, false
)

@Composable
internal fun ChatShimmerScreen(
    modifier: Modifier = Modifier,
    itemCount: Int = shimmerPattern.size,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Spacing.paddingMedium,
        vertical = Spacing.paddingSmall
    ),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(Spacing.paddingSmall),
        reverseLayout = true,
        userScrollEnabled = false,
    ) {
        items(itemCount) { index ->
            MessageBubbleShimmer(
                modifier = Modifier.padding(vertical = Spacing.paddingExtraSmall),
                isIncoming = shimmerPattern[index % shimmerPattern.size],
            )
        }
    }
}
