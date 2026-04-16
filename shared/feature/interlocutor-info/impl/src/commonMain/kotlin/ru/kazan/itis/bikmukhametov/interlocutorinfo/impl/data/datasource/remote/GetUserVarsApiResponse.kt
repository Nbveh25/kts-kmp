package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField

@Serializable
internal data class GetUserVarsApiResponse(
    @SerialName("status") val status: String = "",
    @SerialName("data") val data: GetUserVarsDataDto? = null,
)

@Serializable
internal data class GetUserVarsDataDto(
    @SerialName("vars") val vars: List<UserVarEntryDto> = emptyList(),
)

@Serializable
internal data class UserVarEntryDto(
    @SerialName("key") val key: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null,
    @SerialName("value") val value: JsonElement? = null,
)

internal fun UserVarEntryDto.toModel(): InterlocutorCustomField {
    val label = key?.takeIf { it.isNotBlank() }
        ?: name?.takeIf { it.isNotBlank() }
        ?: slug?.takeIf { it.isNotBlank() }
        ?: ""
    val valueStr = when (val v = value) {
        is JsonPrimitive -> v.content
        null -> ""
        else -> v.toString()
    }
    return InterlocutorCustomField(key = label, value = valueStr)
}
