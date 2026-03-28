package ru.kazan.itis.bikmukhametov.chat.api.usecase

interface UploadChatAttachmentUseCase {
    /** Загрузка файла на `/api/attachments/upload`, возвращает `data._id`. */
    suspend operator fun invoke(
        fileName: String,
        mimeType: String?,
        bytes: ByteArray,
    ): Result<String>
}
