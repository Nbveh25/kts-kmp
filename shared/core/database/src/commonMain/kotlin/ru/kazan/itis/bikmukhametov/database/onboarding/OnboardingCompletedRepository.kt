package ru.kazan.itis.bikmukhametov.database.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingCompletedRepository {
    val onboardingCompleted: Flow<Boolean>
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun setOnboardingCompleted(completed: Boolean)
}

