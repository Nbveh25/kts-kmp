package ru.kazan.itis.bikmukhametov.onboarding.presentation.model

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.DrawableResource

@Immutable
internal data class OnboardingPage(
    val title: String,
    val description: String,
    val buttonText: String,
    val image: DrawableResource,
)
