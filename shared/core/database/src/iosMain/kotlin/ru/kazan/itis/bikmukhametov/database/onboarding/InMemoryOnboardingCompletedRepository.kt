package ru.kazan.itis.bikmukhametov.database.onboarding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory реализация для iOS (при необходимости заменить на Keychain/UserDefaults).
 */
class InMemoryOnboardingCompletedRepository : OnboardingCompletedRepository {
    private val _onboardingCompleted = MutableStateFlow(false)
    override val onboardingCompleted: Flow<Boolean> = _onboardingCompleted.asStateFlow()

    override suspend fun isOnboardingCompleted(): Boolean = _onboardingCompleted.value

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        _onboardingCompleted.value = completed
    }
}
