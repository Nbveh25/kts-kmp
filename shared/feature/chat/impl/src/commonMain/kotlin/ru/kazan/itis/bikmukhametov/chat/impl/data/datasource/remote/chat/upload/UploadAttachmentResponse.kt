package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.upload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadAttachmentApiResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: UploadAttachmentDataDto? = null,
)

@Serializable
data class UploadAttachmentDataDto(
    @SerialName("_id") val id: String,
    @SerialName("filename") val filename: String? = null,
    @SerialName("url") val url: String? = null,
)
