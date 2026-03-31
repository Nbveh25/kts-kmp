package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.UploadChatAttachmentUseCase

internal class UploadChatAttachmentUseCaseImpl(
    private val chatRepository: ChatRepository,
) : UploadChatAttachmentUseCase {

    override suspend fun invoke(
        fileName: String,
        mimeType: String?,
        contentUri: String,
        contentLength: Long?,
    ): Result<String> =
        chatRepository.uploadAttachment(fileName, mimeType, contentUri, contentLength)
}
