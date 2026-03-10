package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.ConversationCardShimmer
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
fun ShimmerScreen(
    modifier: Modifier = Modifier,
    itemCount: Int = 6,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Spacing.paddingMedium,
        vertical = Spacing.paddingSmall
    ),
    cardVerticalPadding: Dp = Spacing.paddingExtraSmall,
    shimmerBuilder: @Composable (Modifier) -> Unit = {
        ConversationCardShimmer(modifier = it)
    }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(itemCount) { index ->
            shimmerBuilder(
                Modifier.padding(vertical = cardVerticalPadding)
            )
        }
    }
}