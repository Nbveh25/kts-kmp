package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation

import ru.kazan.itis.bikmukhametov.chat.api.model.ChannelModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ChannelType
import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationStateModel
import ru.kazan.itis.bikmukhametov.chat.api.model.LastMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.MessageType
import ru.kazan.itis.bikmukhametov.chat.api.model.UserModel

internal fun ConversationDto.toModel(fallbackConversationIdFromRequest: String? = null) = ConversationModel(
    id = when {
        id != 0L -> id
        conversationId != 0L -> conversationId
        else -> fallbackConversationIdFromRequest?.toLongOrNull() ?: 0L
    },
    user = (user ?: participant ?: UserDto()).toModel(),
    channel = (channel ?: ChannelDto()).toModel(),
    state = (state ?: ConversationStateDto()).toModel(),
    lastMessage = lastMessage?.toModel(),
    isRead = isRead,
    updatedAt = dateUpdated
)

internal fun UserDto.toModel() = UserModel(
    id = id,
    username = username.orEmpty(),
    fullName = listOfNotNull(
        firstName?.takeIf { it.isNotBlank() },
        lastName?.takeIf { it.isNotBlank() },
    ).joinToString(" ").ifBlank { username.orEmpty() },
    avatarUrl = photo?.url,
    profileUrl = profileUrl
)

internal fun ChannelDto.toModel() = ChannelModel(
    id = id,
    kind = kind,
    name = name,
    url = url,
    type = when (kind.lowercase()) {
        "chat" -> ChannelType.CHAT
        "email" -> ChannelType.EMAIL
        "telegram" -> ChannelType.TELEGRAM
        else -> ChannelType.UNKNOWN
    }
)

internal fun ConversationStateDto.toModel() = ConversationStateModel(
    isStoppedByManager = stoppedByManager,
    isOperatorTagged = operatorTagged,
    hasUnansweredOperatorMessage = hasUnansweredOperatorMessage
)

internal fun MessageDto.toModel() = LastMessageModel(
    id = id,
    text = text ?: "",
    managerEmail = managerEmail,
    createdAt = dateCreated,
    type = when (kind.lowercase()) {
        "text" -> MessageType.TEXT
        "system" -> MessageType.SYSTEM
        else -> MessageType.UNKNOWN
    }
)
