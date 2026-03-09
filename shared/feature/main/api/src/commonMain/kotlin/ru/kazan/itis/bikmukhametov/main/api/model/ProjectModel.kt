package ru.kazan.itis.bikmukhametov.main.api.model

import kotlin.time.Instant

data class ProjectModel(
    val id: String,
    val name: String,
    val role: ProjectRole?,
    val permissions: List<Permission>,
    val options: ProjectOptions,
    val features: ProjectFeatures,
    val createdAt: String
)

/**
 * Настройки проекта.
 */
data class ProjectOptions(
    val extraBlocks: Map<String, Any?> = emptyMap()
)

/**
 * Функциональные флаги проекта.
 */
data class ProjectFeatures(
    val pgMessages: Boolean = false,
    val rawFeatures: Map<String, Boolean> = emptyMap() // Для будущих флагов
)

// --- Enum-ы для типобезопасности ---

/**
 * Роль пользователя в проекте.
 */
enum class ProjectRole {
    OWNER,
    ADMIN,
    MANAGER,
    DEVELOPER,
    VIEWER,
    UNKNOWN;

    companion object {
        fun fromString(value: String?): ProjectRole? =
            value?.uppercase()?.let { entries.find { role -> role.name == it } } ?: UNKNOWN
    }
}

/**
 * Разрешения (дублируются из Cabinet, можно вынести в общее место).
 */
enum class Permission {
    ACCESS_BILLING,
    EDIT_PROJECT,
    CREATE_PROJECT,
    EDIT_MANAGERS,
    DELETE_PROJECT,
    VIEW_PROJECT,
    VIEW_MANAGERS,
    DELETE_CABINET,
    EDIT_CABINET,
    UNKNOWN;

    companion object {
        fun fromString(value: String): Permission =
            entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}

