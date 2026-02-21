package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import kotlinx.serialization.Serializable

/* Экраны приложения */
@Serializable
sealed interface Route {
    @Serializable
    object Onboarding : Route

    @Serializable
    object Login : Route

}
