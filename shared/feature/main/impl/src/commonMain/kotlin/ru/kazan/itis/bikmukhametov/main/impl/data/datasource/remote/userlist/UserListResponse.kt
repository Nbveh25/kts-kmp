package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.userlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel

@Serializable
internal data class UserListResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: UserListData,
)

@Serializable
internal data class UserListData(
    @SerialName("lists") val lists: List<UserListDto>,
)

@Serializable
internal data class UserListDto(
    @SerialName("_id") val id: String,
    @SerialName("name") val name: String,
    /** Бэкенд не всегда отдаёт tag для списка — без значения по умолчанию падает весь ответ. */
    @SerialName("tag") val tag: String = "",
)

internal fun UserListDto.toModel(): UserListModel = UserListModel(
    id = id,
    name = name,
    tag = tag,
)
