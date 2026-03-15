package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_attachment_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_send_24
import ru.kazan.itis.bikmukhametov.theme.CornerShape
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ChatInputBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onAttachClick: () -> Unit,
    onSendClick: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.paddingSmall, vertical = Spacing.paddingSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onAttachClick) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_attachment_24),
                    contentDescription = "Добавить файлы"
                )
            }
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                placeholder = {
                    Text(
                        text = "Сообщение...",
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                singleLine = false,
                minLines = 1,
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodySmall,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                ),
                shape = RoundedCornerShape(CornerShape.cornerShapeLarge),
            )
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_send_24),
                    tint = Color.White,
                    contentDescription = "Отправить",
                )
            }
        }
    }
}
