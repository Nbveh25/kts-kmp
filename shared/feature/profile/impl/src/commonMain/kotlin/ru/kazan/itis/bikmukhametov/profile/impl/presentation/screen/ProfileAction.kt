package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

internal sealed interface ProfileAction {
    data object Logout : ProfileAction
    data class ToggleNotifications(val enabled: Boolean) : ProfileAction
    data object RetryLoad : ProfileAction
}
