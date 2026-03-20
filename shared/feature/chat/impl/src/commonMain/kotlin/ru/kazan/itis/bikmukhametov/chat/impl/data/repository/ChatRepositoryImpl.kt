package ru.kazan.itis.bikmukhametov.chat.impl.data.repository

import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository

internal class ChatRepositoryImpl(
    private val chatDataSource: ChatDataSource
) : ChatRepository {

    override suspend fun getMessages(
        conversationId: String,
        limit: Int,
        fromId: String?,
        fromDate: String?,
    ): Result<List<ChatMessageModel>> {
        val conversationIdLong = conversationId.toLongOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid conversationId: $conversationId"))

        return chatDataSource.getMessageList(
            conversationId = conversationIdLong,
            limit = limit,
            fromId = fromId,
            fromDate = fromDate,
        )
            .onFailure { error ->
                Napier.e(tag = "ChatRepo", throwable = error) {
                    "Не удалось загрузить сообщения для $conversationId"
                }
            }
    }

    override suspend fun uploadAttachment(
        fileName: String,
        mimeType: String?,
        bytes: ByteArray,
    ): Result<String> =
        chatDataSource.uploadAttachment(fileName, mimeType, bytes)
            .onFailure { error ->
                Napier.e(tag = "ChatRepo", throwable = error) {
                    "Не удалось загрузить вложение $fileName"
                }
            }

    override suspend fun sendMessage(
        conversationId: String,
        messageText: String?,
        attachmentIds: List<String>,
        sendAttachmentAsDocument: Boolean,
    ): Result<Unit> {
        val conversationIdLong = conversationId.toLongOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid conversationId: $conversationId"))

        return chatDataSource.sendMessage(
            conversationId = conversationIdLong,
            messageText = messageText,
            attachmentIds = attachmentIds,
            sendAttachmentAsDocument = sendAttachmentAsDocument,
        )
            .onFailure { error ->
                Napier.e(tag = "ChatRepo", throwable = error) {
                    "Не удалось отправить сообщение в $conversationId"
                }
            }
    }
}
