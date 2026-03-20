package ru.kazan.itis.bikmukhametov.chat.api.usecase

interface SendMessageUseCase {
    /**
     * @param attachmentIds id вложений после [UploadChatAttachmentUseCase]
     * @param sendAttachmentAsDocument «отправить как файл» → `as_document` у каждого элемента `attachments`
     */
    suspend operator fun invoke(
        conversationId: String,
        text: String?,
        attachmentIds: List<String> = emptyList(),
        sendAttachmentAsDocument: Boolean = false,
    ): Result<Unit>
}
