package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage.SendMessageAttachmentItem
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage.SendMessageRequest
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage.SendMessageResponse
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.upload.UploadAttachmentApiResponse
import ru.kazan.itis.bikmukhametov.chat.impl.data.platform.AttachmentContentReader
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class ChatRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val attachmentContentReader: AttachmentContentReader,
) : ChatDataSource {

    override suspend fun uploadAttachment(
        fileName: String,
        mimeType: String?,
        contentUri: String,
        contentLength: Long?,
    ): Result<String> {
        val safeName = fileName
            .replace("\"", "'")
            .replace("\r", "")
            .replace("\n", "")
        val rawResult = runCatchingCancelable {
            val response = httpClient.post(
                urlString = BuildKonfig.BASE_URL + "/api/attachments/upload",
            ) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                FILE,
                                ChannelProvider(size = contentLength) {
                                    attachmentContentReader.openReadChannel(contentUri)
                                        ?: error("upload attachment: cannot open content stream")
                                },
                                Headers.build {
                                    append(
                                        name = HttpHeaders.ContentDisposition,
                                        value = "filename=\"$safeName\"",
                                    )
                                    append(
                                        name = HttpHeaders.ContentType,
                                        value = mimeType ?: "application/octet-stream",
                                    )
                                },
                            )
                        },
                    ),
                )
            }
            val body = response.body<UploadAttachmentApiResponse>()
            body.data?.id ?: error("upload attachment: empty data")
        }
        Napier.d(tag = "ChatApi") { "upload_attachment: success=${rawResult.isSuccess}" }
        return rawResult.mapApiError("Ошибка загрузки вложения")
    }

    override suspend fun sendMessage(
        conversationId: Long,
        messageText: String?,
        attachmentIds: List<String>,
        sendAttachmentAsDocument: Boolean,
    ): Result<Unit> {
        val asDoc = sendAttachmentAsDocument && attachmentIds.isNotEmpty()
        val rawResult = runCatchingCancelable {
            val response = httpClient.post(
                urlString = BuildKonfig.BASE_URL + "/api/conversations/send_message"
            ) {
                setBody(
                    SendMessageRequest(
                        conversationId = conversationId,
                        messageText = messageText?.takeIf { it.isNotBlank() },
                        attachments = attachmentIds.map {
                            SendMessageAttachmentItem(id = it, asDocument = asDoc)
                        },
                    )
                )
            }
            response.body<SendMessageResponse>()
        }.map { response ->
            Napier.d(tag = "ChatApi") {
                "send_message: status=${response.status} conversationId=$conversationId"
            }
        }
        return rawResult.mapApiError("Ошибка отправки сообщения")
    }

    override suspend fun getMessageList(
        conversationId: Long,
        limit: Int,
        fromId: String?,
        fromDate: String?,
    ): Result<List<ChatMessageModel>> {
        val rawResult = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/conversations/list_messages"
            ) {
                url {
                    parameters.append("conversation_id", conversationId.toString())
                    parameters.append("limit", limit.toString())
                    if (fromId != null) parameters.append("from_id", fromId)
                    if (fromDate != null) parameters.append("from_date", fromDate)
                }
            }

            response.body<ChatMessageResponse>()
        }.map { response ->
            val list = response.data.messageList
            Napier.d(tag = "ChatApi") {
                "loaded ${list.size} messages for conversation $conversationId (fromId=$fromId)"
            }
            list.map { it.toModel() }
        }

        return rawResult
            .mapApiError("Ошибка загрузки сообщений чата")
    }

    private companion object {
        const val FILE = "file"
    }
}
