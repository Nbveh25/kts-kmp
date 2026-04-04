package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList

@Serializable
internal data class GetUserListsApiResponse(
    @SerialName("status") val status: String = "",
    @SerialName("data") val data: GetUserListsDataDto? = null,
)

@Serializable
internal data class GetUserListsDataDto(
    @SerialName("lists") val lists: List<UserListEntryDto> = emptyList(),
)

@Serializable
internal data class UserListEntryDto(
    @SerialName("_id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("type") val type: String? = null,
)

internal fun UserListEntryDto.toModel(): InterlocutorUserList? {
    val listId = id?.takeIf { it.isNotBlank() } ?: return null
    return InterlocutorUserList(
        id = listId,
        name = name?.takeIf { it.isNotBlank() } ?: "—",
        type = type?.takeIf { it.isNotBlank() } ?: "",
    )
}
