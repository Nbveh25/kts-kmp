package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent

@Serializable
internal data class GetUserPlannedEventsApiResponse(
    @SerialName("status") val status: String = "",
    @SerialName("data") val data: GetUserPlannedEventsDataDto? = null,
)

@Serializable
internal data class GetUserPlannedEventsDataDto(
    @SerialName("events") val events: List<PlannedEventEntryDto> = emptyList(),
)

@Serializable
internal data class PlannedEventEntryDto(
    @SerialName("_id") val id: String? = null,
    @SerialName("id") val legacyId: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("scenario_name") val scenarioName: String? = null,
    @SerialName("scenario_title") val scenarioTitle: String? = null,
    @SerialName("fire_at") val fireAt: String? = null,
    @SerialName("scheduled_at") val scheduledAt: String? = null,
    @SerialName("run_at") val runAt: String? = null,
    @SerialName("date") val date: String? = null,
)

internal fun PlannedEventEntryDto.toModel(): InterlocutorPlannedEvent? {
    val eventId = id?.takeIf { it.isNotBlank() }
        ?: legacyId?.takeIf { it.isNotBlank() }
        ?: return null
    val titleLine = sequenceOf(title, name, scenarioName, scenarioTitle)
        .mapNotNull { it?.takeIf { s -> s.isNotBlank() } }
        .firstOrNull()
        ?.trim()
        .orEmpty()
        .ifBlank { "Событие" }
    val timeLine = sequenceOf(fireAt, scheduledAt, runAt, date)
        .mapNotNull { it?.takeIf { s -> s.isNotBlank() } }
        .firstOrNull()
        ?.trim()
        .orEmpty()
    return InterlocutorPlannedEvent(
        id = eventId,
        title = titleLine,
        scheduledAt = timeLine,
    )
}
