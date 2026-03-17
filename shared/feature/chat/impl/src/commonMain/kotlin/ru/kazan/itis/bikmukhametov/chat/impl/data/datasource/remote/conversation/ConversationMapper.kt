package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation

import ru.kazan.itis.bikmukhametov.chat.api.model.ChannelModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ChannelType
import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ConversationStateModel
import ru.kazan.itis.bikmukhametov.chat.api.model.LastMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.MessageType
import ru.kazan.itis.bikmukhametov.chat.api.model.UserModel

internal fun ConversationDto.toModel() = ConversationModel(
    id = id,
    user = user.toModel(),
    channel = channel.toModel(),
    state = state.toModel(),
    lastMessage = lastMessage?.toModel(),
    isRead = isRead,
    updatedAt = dateUpdated
)

internal fun UserDto.toModel() = UserModel(
    id = id,
    username = username,
    // Объединяем имя и фамилию сразу при маппинге
    fullName = "$firstName $lastName".trim(),
    avatarUrl = photo?.url,
    profileUrl = profileUrl
)

internal fun ChannelDto.toModel() = ChannelModel(
    id = id,
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
