package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    object Onboarding : Route

    @Serializable
    object Login : Route
}
