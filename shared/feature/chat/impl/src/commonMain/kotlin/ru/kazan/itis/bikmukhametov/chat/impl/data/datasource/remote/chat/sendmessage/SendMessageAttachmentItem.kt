package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.sendmessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SendMessageAttachmentItem(
    @SerialName("_id") val id: String,
    @SerialName("as_document") val asDocument: Boolean = false,
)
