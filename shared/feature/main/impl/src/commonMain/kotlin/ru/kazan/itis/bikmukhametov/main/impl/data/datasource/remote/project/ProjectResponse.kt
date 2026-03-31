package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.main.api.model.space.Permission
import ru.kazan.itis.bikmukhametov.main.api.model.space.ProjectModel
import ru.kazan.itis.bikmukhametov.main.api.model.space.ProjectRole
import ru.kazan.itis.bikmukhametov.main.api.model.space.ProjectOptions
import ru.kazan.itis.bikmukhametov.main.api.model.space.ProjectFeatures

@Serializable
data class ProjectResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: ProjectData
)

@Serializable
data class ProjectData(
    @SerialName("projects") val projects: List<ProjectDto>
)

@Serializable
data class ProjectDto(
    @SerialName("_id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("project_role") val projectRole: String?,
    @SerialName("permissions") val permissions: List<String>,
    @SerialName("options") val options: ProjectOptionsDto,
    @SerialName("features") val features: ProjectFeaturesDto,
    @SerialName("date_created") val dateCreated: String
)

@Serializable
data class ProjectOptionsDto(
    @SerialName("extra_blocks") val extraBlocks: Map<String, String> = emptyMap()
)

@Serializable
data class ProjectFeaturesDto(
    @SerialName("PG_MESSAGES") val pgMessages: Boolean = false
)

fun ProjectDto.toModel(): ProjectModel {
    val projectModel = ProjectModel(
        id = this.id,
        name = this.name,
        role = ProjectRole.fromString(this.projectRole),
        permissions = this.permissions.map { Permission.fromString(it) },
        options = this.options.toModel(),
        features = this.features.toModel(),
        createdAt = this.dateCreated
    )
    return projectModel
}

fun ProjectOptionsDto.toModel(): ProjectOptions = ProjectOptions(
    extraBlocks = this.extraBlocks
)

fun ProjectFeaturesDto.toModel(): ProjectFeatures = ProjectFeatures(
    pgMessages = this.pgMessages,
    rawFeatures = emptyMap()
)
