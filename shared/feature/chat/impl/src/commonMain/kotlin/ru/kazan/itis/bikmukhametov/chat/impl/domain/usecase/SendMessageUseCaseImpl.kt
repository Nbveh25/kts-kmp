package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.SendMessageUseCase

internal class SendMessageUseCaseImpl(
    private val chatRepository: ChatRepository
) : SendMessageUseCase {

    override suspend fun invoke(
        conversationId: String,
        text: String?,
        attachmentIds: List<String>,
        sendAttachmentAsDocument: Boolean,
    ): Result<Unit> =
        chatRepository.sendMessage(
            conversationId = conversationId,
            messageText = text,
            attachmentIds = attachmentIds,
            sendAttachmentAsDocument = sendAttachmentAsDocument,
        )
}
