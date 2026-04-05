package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.SenderType
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig

@Serializable
data class ChatMessageResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: MessageData
)

@Serializable
data class MessageData(
    @SerialName("messages") val messages: List<MessageDto> = emptyList(),
    @SerialName("items") val items: List<MessageDto> = emptyList(),
) {
    val messageList: List<MessageDto> get() = messages.ifEmpty { items }
}

@Serializable
data class MessageDto(
    @SerialName("id") val id: String,
    @SerialName("conversation_id") val conversationId: Long,
    @SerialName("text") val text: String? = null,
    @SerialName("kind") val kind: String, // "user", "bot", "service"
    @SerialName("date_created") val dateCreated: String,
    @SerialName("manager_email") val managerEmail: String? = null,
    @SerialName("block_id") val blockId: String? = null,
    @SerialName("scenario_id") val scenarioId: String? = null,
    @SerialName("bucket") val bucket: String? = null,
    @SerialName("attachments") val attachments: List<ChatMessageAttachmentDto> = emptyList(),
)

@Serializable
data class ChatMessageAttachmentDto(
    @SerialName("_id") val id: String? = null,
    @SerialName("filename") val filename: String? = null,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("link") val link: String? = null,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("as_document") val asDocument: Boolean? = null,
    @SerialName("type") val type: String? = null,
)

internal fun MessageDto.toModel(): ChatMessageModel {
    val senderType = when (kind.lowercase()) {
        "user" -> SenderType.USER
        "bot" -> SenderType.BOT
        "service" -> SenderType.SERVICE
        else -> SenderType.UNKNOWN
    }

    val imageUrls = attachments.mapNotNull { it.toAbsoluteImagePreviewUrl() }

    return ChatMessageModel(
        id = id,
        text = text.orEmpty(),
        senderType = senderType,
        createdAt = dateCreated,
        managerEmail = managerEmail,
        imageAttachmentUrls = imageUrls,
    )
}

private fun ChatMessageAttachmentDto.toAbsoluteImagePreviewUrl(): String? {
    if (asDocument == true) return null
    if (!looksLikeImage()) return null

    val raw = sequenceOf(previewUrl, url, link)
        .filterNotNull()
        .firstOrNull { it.isNotBlank() }
        ?.trim()
        ?: id?.takeIf { it.isNotBlank() }?.let { fallbackAttachmentFileUrl(it) }
        ?: return null

    return absolutizeCabinetUrl(raw)
}

private fun ChatMessageAttachmentDto.looksLikeImage(): Boolean {
    val t = type?.lowercase().orEmpty()
    if (t.startsWith("image/")) return true
    val f = filename?.lowercase().orEmpty()
    return f.endsWith(".jpg") || f.endsWith(".jpeg") || f.endsWith(".png") ||
        f.endsWith(".gif") || f.endsWith(".webp") || f.endsWith(".bmp") ||
        f.endsWith(".heic") || f.endsWith(".heif")
}

private fun fallbackAttachmentFileUrl(id: String): String =
    "${BuildKonfig.BASE_URL.trimEnd('/')}/api/attachments/$id"

private fun absolutizeCabinetUrl(raw: String): String {
    if (raw.startsWith("http://", ignoreCase = true) ||
        raw.startsWith("https://", ignoreCase = true)
    ) {
        return raw
    }
    val base = BuildKonfig.BASE_URL.trimEnd('/')
    val path = raw.trimStart('/')
    return "$base/$path"
}
