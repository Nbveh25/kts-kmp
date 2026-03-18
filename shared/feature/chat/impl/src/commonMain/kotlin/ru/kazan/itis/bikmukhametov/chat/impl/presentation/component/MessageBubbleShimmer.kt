package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun MessageBubbleShimmer(
    modifier: Modifier = Modifier,
    isIncoming: Boolean = true,
) {
    val baseColor = MaterialTheme.colorScheme.surfaceVariant
    val highlightColor = baseColor.copy(alpha = 0.4f)

    val transition = rememberInfiniteTransition(label = "bubble_shimmer_transition")
    val shimmerOffsetX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubble_shimmer_offset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(shimmerOffsetX, 0f),
        end = Offset(shimmerOffsetX + 300f, 0f)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isIncoming) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (isIncoming) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(shimmerBrush)
            )
            Spacer(modifier = Modifier.width(Spacing.paddingSmall))
        }

        Column(
            modifier = Modifier.widthIn(min = 80.dp, max = 220.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.paddingExtraSmall)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isIncoming) 4.dp else 16.dp,
                            bottomEnd = if (isIncoming) 16.dp else 4.dp,
                        )
                    )
                    .background(shimmerBrush)
            )
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(shimmerBrush)
                    .align(if (isIncoming) Alignment.Start else Alignment.End)
                    .padding(top = 2.dp)
            )
        }

        if (!isIncoming) {
            Spacer(modifier = Modifier.width(Spacing.paddingSmall))
        }
    }
}
