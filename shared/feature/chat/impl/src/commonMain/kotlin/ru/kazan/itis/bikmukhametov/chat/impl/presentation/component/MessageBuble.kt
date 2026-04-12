package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatFileAttachment
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_bot
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_k
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_operator
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_start_bot
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_stop_bot
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_open_attachment
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_attachment_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_bot
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_manager
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.ChatMessageItem
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.imageUrlsForBubble
import ru.kazan.itis.bikmukhametov.theme.CornerShape
import ru.kazan.itis.bikmukhametov.theme.Dimensions
import ru.kazan.itis.bikmukhametov.theme.SmartbotSuccess
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun MessageBubble(
    message: ChatMessageItem,
    showAvatar: Boolean,
    interlocutorAvatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    val isSystem = message.sender == SenderType.SERVICE || message.sender == SenderType.UNKNOWN

    if (isSystem) {
        SystemBubble(message = message, modifier = modifier)
        return
    }

    // USER — слева, BOT — справа
    val isLeftAligned = message.sender == SenderType.USER

    val interlocutorBubbleBackground =
        if (isSystemInDarkTheme()) SmartbotSuccess.copy(alpha = 0.30f)
        else SmartbotSuccess.copy(alpha = 0.18f)

    val backgroundColor = when (message.sender) {
        SenderType.BOT -> MaterialTheme.colorScheme.primaryContainer
        else -> interlocutorBubbleBackground
    }
    val textColor = when (message.sender) {
        SenderType.BOT -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    // Хвостик пузыря появляется только у последнего сообщения в группе (showAvatar = true)
    val shape = RoundedCornerShape(
        topStart = CornerShape.cornerShapeMedium,
        topEnd = CornerShape.cornerShapeMedium,
        bottomStart = if (isLeftAligned && showAvatar) 4.dp else CornerShape.cornerShapeMedium,
        bottomEnd = if (!isLeftAligned && showAvatar) 4.dp else CornerShape.cornerShapeMedium,
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (isLeftAligned) Arrangement.Start else Arrangement.End,
    ) {
        if (isLeftAligned) {
            AvatarSlot(
                sender = message.sender,
                show = showAvatar,
                managerEmail = message.managerEmail,
                interlocutorAvatarUrl = interlocutorAvatarUrl,
            )
            Spacer(Modifier.width(Spacing.paddingSmall))
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = if (isLeftAligned) Alignment.Start else Alignment.End,
        ) {
            Surface(shape = shape, color = backgroundColor) {
                Column(modifier = Modifier.padding(Spacing.paddingSmall)) {
                    message.imageUrlsForBubble().forEach { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(bottom = Spacing.paddingExtraSmall)
                                .fillMaxWidth()
                                .heightIn(max = 220.dp)
                                .clip(RoundedCornerShape(CornerShape.cornerShapeSmall)),
                        )
                    }
                    message.fileAttachments.forEach { file ->
                        FileAttachmentRow(
                            file = file,
                            textColor = textColor,
                            modifier = Modifier.padding(bottom = Spacing.paddingExtraSmall),
                        )
                    }
                    val bodyText = resolveText(message)
                    if (bodyText.isNotBlank()) {
                        Text(
                            text = bodyText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                        )
                    }
                    Text(
                        text = message.createdAt,
                        modifier = Modifier.padding(top = Spacing.paddingExtraSmall),
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.7f),
                    )
                }
            }
        }

        if (!isLeftAligned) {
            Spacer(Modifier.width(Spacing.paddingSmall))
            AvatarSlot(
                sender = message.sender,
                show = showAvatar,
                managerEmail = message.managerEmail,
                interlocutorAvatarUrl = interlocutorAvatarUrl,
            )
        }
    }
}

@Composable
private fun SystemBubble(message: ChatMessageItem, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            shape = RoundedCornerShape(CornerShape.cornerShapeMedium),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = Spacing.paddingMedium,
                    vertical = Spacing.paddingExtraSmall,
                ),
                text = resolveText(message),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Показывает аватар или пустое место той же ширины для выравнивания пузырей. */
@Composable
private fun AvatarSlot(
    sender: SenderType,
    show: Boolean,
    managerEmail: String?,
    interlocutorAvatarUrl: String?,
) {
    if (show) {
        SenderAvatar(
            sender = sender,
            managerEmail = managerEmail,
            interlocutorAvatarUrl = interlocutorAvatarUrl,
        )
    } else {
        Spacer(modifier = Modifier.size(Dimensions.chatAvatarSize))
    }
}

/**
 * Аватар отправителя:
 * - USER → фото собеседника через AsyncImage
 * - BOT + managerEmail != null → иконка оператора (ic_manager)
 * - BOT + managerEmail == null → иконка бота (ic_bot)
 */
@Composable
private fun SenderAvatar(
    sender: SenderType,
    managerEmail: String?,
    interlocutorAvatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    val baseModifier = modifier
        .size(Dimensions.chatAvatarSize)
        .clip(CircleShape)

    when (sender) {
        SenderType.USER -> {
            if (interlocutorAvatarUrl != null) {
                AsyncImage(
                    model = interlocutorAvatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = baseModifier,
                )
            } else {
                val placeholderBg =
                    if (isSystemInDarkTheme()) SmartbotSuccess.copy(alpha = 0.38f)
                    else SmartbotSuccess.copy(alpha = 0.26f)
                Box(
                    modifier = baseModifier.background(placeholderBg),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.chat_k),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        SenderType.BOT -> {
            Box(
                modifier = baseModifier.background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = if (managerEmail != null) {
                        painterResource(Res.drawable.ic_manager)
                    } else {
                        painterResource(Res.drawable.ic_bot)
                    },
                    contentDescription = if (managerEmail != null) stringResource(Res.string.chat_operator)
                    else stringResource(Res.string.chat_bot),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        else -> Unit // SERVICE, UNKNOWN — без аватара
    }
}

@Composable
private fun FileAttachmentRow(
    file: ChatFileAttachment,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val openUrl = file.openUrl
    val openLabel = stringResource(Res.string.chat_open_attachment)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (openUrl != null) {
                    Modifier.clickable { uriHandler.openUri(openUrl) }
                } else {
                    Modifier
                },
            ),
        shape = RoundedCornerShape(CornerShape.cornerShapeSmall),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
    ) {
        Row(
            modifier = Modifier.padding(Spacing.paddingSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_attachment_24),
                contentDescription = if (openUrl != null) openLabel else null,
                tint = textColor.copy(alpha = 0.9f),
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(Spacing.paddingSmall))
            Column(Modifier.weight(1f)) {
                Text(
                    text = file.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    maxLines = 2,
                )
                file.sizeBytes?.let { bytes ->
                    Text(
                        text = formatAttachmentFileSize(bytes),
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.75f),
                    )
                }
            }
        }
    }
}

private fun formatAttachmentFileSize(bytes: Int): String {
    val unit = 1024
    return when {
        bytes < unit -> "$bytes Б"
        bytes < unit * unit -> "${bytes / unit} КБ"
        else -> {
            val mb = bytes.toDouble() / (unit * unit)
            val rounded = (mb * 10).roundToInt() / 10.0
            "$rounded МБ"
        }
    }
}

@Composable
private fun resolveText(message: ChatMessageItem): String = when (message.text) {
    "stop_bot" -> stringResource(Res.string.chat_stop_bot)
    "start_bot" -> stringResource(Res.string.chat_start_bot)
    else -> message.text
}
