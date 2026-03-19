package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.bot

import kotlinx.serialization.Serializable

@Serializable
data class BotRequest(val conversationId: String)
