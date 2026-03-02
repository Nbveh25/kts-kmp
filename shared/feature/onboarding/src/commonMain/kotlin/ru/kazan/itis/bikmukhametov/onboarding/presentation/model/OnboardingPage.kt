package ru.kazan.itis.bikmukhametov.onboarding.presentation.model

import org.jetbrains.compose.resources.DrawableResource

internal data class OnboardingPage(
    val title: String,
    val description: String,
    val buttonText: String,
    val image: DrawableResource,
)
