package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.local

import ru.kazan.itis.bikmukhametov.database.room.conversation.ConversationEntity
import ru.kazan.itis.bikmukhametov.main.api.model.AttachmentModel
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelModel
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationStateModel
import ru.kazan.itis.bikmukhametov.main.api.model.LastMessageModel
import ru.kazan.itis.bikmukhametov.main.api.model.MessageKind
import ru.kazan.itis.bikmukhametov.main.api.model.PhotoModel
import ru.kazan.itis.bikmukhametov.main.api.model.UserModel

internal fun ConversationModel.toEntity(): ConversationEntity = ConversationEntity(
    id = id,
    dateUpdated = dateUpdated,
    isRead = isRead,
    userId = user.id,
    userUsername = user.username,
    userFirstName = user.firstName,
    userLastName = user.lastName,
    userPhotoUrl = user.photo?.url,
    userUrl = user.url,
    channelId = channel.id,
    channelKind = channel.kind.name,
    channelName = channel.name,
    channelUrl = channel.url,
    stoppedByManager = state.stoppedByManager,
    operatorTagged = state.operatorTagged,
    hasUnansweredOperatorMessage = state.hasUnansweredOperatorMessage,
    lastMessageId = lastMessage?.id,
    lastMessageConversationId = lastMessage?.conversationId,
    lastMessageText = lastMessage?.text,
    lastMessageKind = lastMessage?.kind?.name,
    lastMessageBlockId = lastMessage?.blockId,
    lastMessageScenarioId = lastMessage?.scenarioId,
    lastMessageBucket = lastMessage?.bucket,
    lastMessageDateCreated = lastMessage?.dateCreated,
    lastMessageManagerEmail = lastMessage?.managerEmail,
    lastMessageIsRead = lastMessage?.isRead,
    attachmentAsDocument = lastMessage?.attachments?.asDocument,
    attachmentFilename = lastMessage?.attachments?.filename,
    attachmentPreviewUrl = lastMessage?.attachments?.previewUrl,
    attachmentSize = lastMessage?.attachments?.size,
    attachmentType = lastMessage?.attachments?.type,
    attachmentUrl = lastMessage?.attachments?.url,
    attachmentWidth = lastMessage?.attachments?.width,
    attachmentHeight = lastMessage?.attachments?.height,
)

internal fun ConversationEntity.toModel(): ConversationModel = ConversationModel(
    id = id,
    dateUpdated = dateUpdated,
    isRead = isRead,
    user = UserModel(
        id = userId,
        username = userUsername,
        firstName = userFirstName,
        lastName = userLastName,
        photo = userPhotoUrl?.let { PhotoModel(url = it) },
        url = userUrl,
    ),
    channel = ChannelModel(
        id = channelId,
        kind = ChannelKind.fromString(channelKind),
        name = channelName,
        url = channelUrl,
    ),
    state = ConversationStateModel(
        stoppedByManager = stoppedByManager,
        operatorTagged = operatorTagged,
        hasUnansweredOperatorMessage = hasUnansweredOperatorMessage,
    ),
    lastMessage = if (lastMessageId != null && lastMessageDateCreated != null) {
        LastMessageModel(
            id = lastMessageId!!,
            conversationId = lastMessageConversationId ?: 0L,
            text = lastMessageText,
            kind = MessageKind.fromString(lastMessageKind),
            blockId = lastMessageBlockId,
            scenarioId = lastMessageScenarioId,
            bucket = lastMessageBucket,
            dateCreated = lastMessageDateCreated!!,
            attachments = if (attachmentType != null || attachmentUrl != null) {
                AttachmentModel(
                    asDocument = attachmentAsDocument,
                    filename = attachmentFilename,
                    previewUrl = attachmentPreviewUrl,
                    size = attachmentSize,
                    type = attachmentType,
                    url = attachmentUrl,
                    width = attachmentWidth,
                    height = attachmentHeight,
                )
            } else null,
            managerEmail = lastMessageManagerEmail,
            isRead = lastMessageIsRead,
            extra = null,
        )
    } else null,
)
