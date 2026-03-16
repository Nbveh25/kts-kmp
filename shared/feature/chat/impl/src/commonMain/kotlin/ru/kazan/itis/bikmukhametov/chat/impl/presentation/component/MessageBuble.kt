package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.ChatMessageItem
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.MessageSender
import ru.kazan.itis.bikmukhametov.theme.CornerShape
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun MessageBubble(
    message: ChatMessageItem,
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
