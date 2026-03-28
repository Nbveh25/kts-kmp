package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class SendMessageResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: JsonElement? = null
)
