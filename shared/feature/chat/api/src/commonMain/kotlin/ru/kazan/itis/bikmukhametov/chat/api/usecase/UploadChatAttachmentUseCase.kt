package ru.kazan.itis.bikmukhametov.chat.api.usecase

interface UploadChatAttachmentUseCase {
    suspend operator fun invoke(
        fileName: String,
        mimeType: String?,
        contentUri: String,
        contentLength: Long?,
    ): Result<String>
}
