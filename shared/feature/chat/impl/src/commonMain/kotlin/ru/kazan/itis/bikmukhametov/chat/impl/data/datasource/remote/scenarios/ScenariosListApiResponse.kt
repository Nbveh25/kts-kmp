package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.scenarios

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.chat.api.model.ScenarioModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ScenariosListResult

@Serializable
internal data class ScenariosListApiResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: ScenariosListDataDto? = null,
)

@Serializable
internal data class ScenariosListDataDto(
    @SerialName("scenarios") val scenarios: List<ScenarioItemDto> = emptyList(),
    @SerialName("total") val total: Int = 0,
    @SerialName("has_changes") val hasChanges: Boolean = false,
)

@Serializable
internal data class ScenarioItemDto(
    @SerialName("_id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("channel_ids") val channelIds: List<String>? = null,
    @SerialName("enabled_in") val enabledIn: EnabledInDto? = null,
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("has_changes") val hasChanges: Boolean = false,
    @SerialName("has_been_published") val hasBeenPublished: Boolean = false,
    @SerialName("update_id") val updateId: String? = null,
    @SerialName("folder_id") val folderId: String? = null,
    @SerialName("date_created") val dateCreated: String? = null,
    @SerialName("date_updated") val dateUpdated: String? = null,
    @SerialName("kind") val kind: String = "",
)

@Serializable
internal data class EnabledInDto(
    @SerialName("dev") val dev: Boolean = false,
    @SerialName("prod") val prod: Boolean = false,
)

internal fun ScenarioItemDto.toModel(): ScenarioModel = ScenarioModel(
    id = id,
    name = name,
    kind = kind,
)

internal fun ScenariosListDataDto.toResult(): ScenariosListResult = ScenariosListResult(
    scenarios = scenarios.map { it.toModel() },
    total = total,
)
