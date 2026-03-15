package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.ChatMessageUi
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.MessageSender
import ru.kazan.itis.bikmukhametov.theme.CornerShape
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun MessageBubble(
    message: ChatMessageUi,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, textColor, alignment, horizontalArrangement) = when (message.sender) {
        MessageSender.OPERATOR -> Quadruple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            Alignment.Start,
            Arrangement.Start,
        )
        MessageSender.CLIENT -> Quadruple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            Alignment.End,
            Arrangement.End,
        )
        MessageSender.SYSTEM -> Quadruple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            Alignment.CenterHorizontally,
            Arrangement.Center,
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = CornerShape.cornerShapeMedium,
                topEnd = CornerShape.cornerShapeMedium,
                bottomStart = if (message.sender == MessageSender.CLIENT) CornerShape.cornerShapeMedium else 4.dp,
                bottomEnd = if (message.sender == MessageSender.OPERATOR) CornerShape.cornerShapeMedium else 4.dp,
            ),
            color = backgroundColor,
        ) {
            Column(
                modifier = Modifier.padding(Spacing.paddingMedium),
            ) {
                if (message.imageUrl != null) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model = message.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(Spacing.paddingSmall))
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
