package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.conversation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.main.api.model.AttachmentModel
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelModel
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationStateModel
import ru.kazan.itis.bikmukhametov.main.api.model.LastMessageModel
import ru.kazan.itis.bikmukhametov.main.api.model.MessageKind
import ru.kazan.itis.bikmukhametov.main.api.model.PhotoModel
import ru.kazan.itis.bikmukhametov.main.api.model.UserModel

/**
 * Корневой ответ от сервера для списка чатов
 */
@Serializable
data class ConversationResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: ConversationData
)

/**
 * Обертка для списка диалогов
 */
@Serializable
data class ConversationData(
    @SerialName("conversations") val conversations: List<ConversationDto>
)

/**
 * Модель одного диалога (чата)
 */
@Serializable
data class ConversationDto(
    @SerialName("user") val user: UserDto,
    @SerialName("channel") val channel: ChannelDto,
    @SerialName("state") val state: ConversationStateDto,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("last_message") val lastMessage: LastMessageDto?,
    @SerialName("id") val id: Long,
    @SerialName("date_updated") val dateUpdated: String
)

/**
 * Информация о пользователе в чате
 */
@Serializable
data class UserDto(
    @SerialName("_id") val id: String,
    @SerialName("username") val username: String?,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("photo") val photo: PhotoDto?,
    @SerialName("url") val url: String?
)

/**
 * Фотография пользователя
 */
@Serializable
data class PhotoDto(
    @SerialName("url") val url: String?
)

/**
 * Информация о канале (источнике чата)
 */
@Serializable
data class ChannelDto(
    @SerialName("_id") val id: String,
    @SerialName("kind") val kind: String, // "tg", "wa", etc.
    @SerialName("name") val name: String?,
    @SerialName("url") val url: String?
)

/**
 * Состояние диалога
 */
@Serializable
data class ConversationStateDto(
    @SerialName("stopped_by_manager") val stoppedByManager: Boolean,
    @SerialName("operator_tagged") val operatorTagged: Boolean,
    @SerialName("has_unanswered_operator_message") val hasUnansweredOperatorMessage: Boolean
)

/**
 * Последнее сообщение в диалоге
 */
@Serializable
data class LastMessageDto(
    @SerialName("id") val id: String,
    @SerialName("conversation_id") val conversationId: Long,
    @SerialName("text") val text: String?,
    @SerialName("kind") val kind: String?, // "bot", "user", "service"
    @SerialName("block_id") val blockId: String?,
    @SerialName("scenario_id") val scenarioId: String?,
    @SerialName("bucket") val bucket: String?,
    @SerialName("date_created") val dateCreated: String,
    // Опциональные поля из Postman-схемы (могут отсутствовать в реальном ответе)
    @SerialName("attachments") val attachments: AttachmentDto? = null,
    @SerialName("manager_email") val managerEmail: String? = null,
    @SerialName("is_read") val isRead: Boolean? = null,
    @SerialName("extra") val extra: Map<String, String>? = null // тут вместо второго string - any
)

/**
 * Вложение в сообщении (опционально)
 */
@Serializable
data class AttachmentDto(
    @SerialName("as_document") val asDocument: Boolean?,
    @SerialName("filename") val filename: String?,
    @SerialName("preview_url") val previewUrl: String?,
    @SerialName("size") val size: Int?,
    @SerialName("type") val type: String?,
    @SerialName("url") val url: String?,
    @SerialName("width") val width: Int?,
    @SerialName("height") val height: Int?
)

fun ConversationDto.toModel(): ConversationModel = ConversationModel(
    id = id,
    user = user.toModel(),
    channel = channel.toModel(),
    state = state.toModel(),
    isRead = isRead,
    lastMessage = lastMessage?.toModel(),
    dateUpdated = dateUpdated
)

fun UserDto.toModel(): UserModel = UserModel(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    photo = photo?.toModel(),
    url = url
)

fun PhotoDto.toModel(): PhotoModel = PhotoModel(
    url = url
)

fun ChannelDto.toModel(): ChannelModel = ChannelModel(
    id = id,
    kind = ChannelKind.fromString(kind),
    name = name,
    url = url
)

fun ConversationStateDto.toModel(): ConversationStateModel = ConversationStateModel(
    stoppedByManager = stoppedByManager,
    operatorTagged = operatorTagged,
    hasUnansweredOperatorMessage = hasUnansweredOperatorMessage
)

fun LastMessageDto.toModel(): LastMessageModel = LastMessageModel(
    id = id,
    conversationId = conversationId,
    text = text,
    kind = MessageKind.fromString(kind),
    blockId = blockId,
    scenarioId = scenarioId,
    bucket = bucket,
    dateCreated = dateCreated,
    attachments = attachments?.toModel(),
    managerEmail = managerEmail,
    isRead = isRead,
    extra = extra // Map<String, String>? совместим с Map<String, Any>?
)

fun AttachmentDto.toModel(): AttachmentModel = AttachmentModel(
    asDocument = asDocument,
    filename = filename,
    previewUrl = previewUrl,
    size = size,
    type = type,
    url = url,
    width = width,
    height = height
)