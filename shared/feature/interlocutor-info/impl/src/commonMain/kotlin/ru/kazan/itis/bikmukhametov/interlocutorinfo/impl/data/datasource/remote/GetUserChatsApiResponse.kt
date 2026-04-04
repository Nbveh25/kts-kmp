package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat

@Serializable
internal data class GetUserChatsApiResponse(
    @SerialName("status") val status: String = "",
    @SerialName("data") val data: GetUserChatsDataDto? = null,
)

@Serializable
internal data class GetUserChatsDataDto(
    @SerialName("chats") val chats: List<UserChatEntryDto> = emptyList(),
)

@Serializable
internal data class UserChatEntryDto(
    @SerialName("_id") val id: String? = null,
    @SerialName("external_id") val externalId: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("channel_kind") val channelKind: String? = null,
    @SerialName("channel_id") val channelId: String? = null,
    @SerialName("realm") val realm: String? = null,
    @SerialName("is_enabled") val isEnabled: Boolean? = null,
)

internal fun UserChatEntryDto.toModel(): InterlocutorUserChat? {
    val chatId = id?.takeIf { it.isNotBlank() } ?: return null
    val ext = externalId.orEmpty()
    val ttl = title?.takeIf { it.isNotBlank() } ?: ext.ifBlank { "—" }
    return InterlocutorUserChat(
        id = chatId,
        externalId = ext,
        title = ttl,
        channelKind = channelKind.orEmpty(),
        channelId = channelId.orEmpty(),
        realm = realm.orEmpty(),
        isEnabled = isEnabled ?: true,
    )
}
