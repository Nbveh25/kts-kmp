package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import kotlinx.serialization.Serializable

/* Экраны приложения */
@Serializable
sealed interface Route {
    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Login : Route

}
