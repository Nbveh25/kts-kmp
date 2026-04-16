package ru.kazan.itis.bikmukhametov.chat.api.datasource

import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface ChatDataSource {
    suspend fun getMessageList(
        conversationId: Long,
        limit: Int,
        fromId: String? = null,
        fromDate: String? = null,
    ): Result<List<ChatMessageModel>>

    /** Multipart `file` (поток) → ответ `data._id` для элементов `send_message.attachments` (`_id` + `as_document`). */
    suspend fun uploadAttachment(
        fileName: String,
        mimeType: String?,
        contentUri: String,
        contentLength: Long?,
    ): Result<String>

    suspend fun sendMessage(
        conversationId: Long,
        messageText: String?,
        attachmentIds: List<String> = emptyList(),
        sendAttachmentAsDocument: Boolean = false,
    ): Result<Unit>
}
