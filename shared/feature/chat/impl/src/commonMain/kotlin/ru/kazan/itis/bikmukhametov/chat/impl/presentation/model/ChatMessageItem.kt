package ru.kazan.itis.bikmukhametov.chat.impl.presentation.model

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatFileAttachment
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType
import ru.kazan.itis.bikmukhametov.ui.util.epochDayOf
import ru.kazan.itis.bikmukhametov.ui.util.formatTimeOnly

/* Модель сообщения для UI */
@Immutable
internal data class ChatMessageItem(
    val id: String,
    val text: String,
    val sender: SenderType,
    val createdAt: String,   // время в формате "HH:MM"
    val epochDay: Long,      // день (epochMs / 86_400_000) для группировки по датам
    val managerEmail: String? = null,
    val imageAttachmentUrls: List<String> = emptyList(),
    val localImagePreviewUris: List<String> = emptyList(),
    val fileAttachments: List<ChatFileAttachment> = emptyList(),
)

internal fun ChatMessageModel.toItem(): ChatMessageItem {
    return ChatMessageItem(
        id = id,
        text = text,
        sender = senderType,
        createdAt = formatTimeOnly(createdAt),
        epochDay = epochDayOf(createdAt),
        managerEmail = managerEmail,
        imageAttachmentUrls = imageAttachmentUrls,
        localImagePreviewUris = localImagePreviewUris,
        fileAttachments = fileAttachments,
    )
}

/** URL для превью в пузыре: локальный контент (оптимистичная отправка) или сетевой. */
internal fun ChatMessageItem.imageUrlsForBubble(): List<String> {
    if (localImagePreviewUris.isEmpty()) return imageAttachmentUrls
    val count = maxOf(localImagePreviewUris.size, imageAttachmentUrls.size)
    return List(count) { i ->
        val local = localImagePreviewUris.getOrNull(i)?.takeIf { it.isNotBlank() }
        val remote = imageAttachmentUrls.getOrNull(i)?.takeIf { it.isNotBlank() }
        local ?: remote.orEmpty()
    }.filter { it.isNotBlank() }
}
