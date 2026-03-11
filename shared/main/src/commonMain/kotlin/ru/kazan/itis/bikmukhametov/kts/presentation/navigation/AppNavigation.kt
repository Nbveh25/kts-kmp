package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import ru.kazan.itis.bikmukhametov.database.onboarding.OnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginScreen
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainScreen
import ru.kazan.itis.bikmukhametov.network.auth.LogoutEventBus
import ru.kazan.itis.bikmukhametov.network.auth.SessionChecker
import ru.kazan.itis.bikmukhametov.onboarding.presentation.screens.OnboardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Route = Route.Onboarding,
) {
    val scope = rememberCoroutineScope()
    val logoutEventBus = koinInject<LogoutEventBus>()
    val sessionChecker = koinInject<SessionChecker>()
    val onboardingRepository = koinInject<OnboardingCompletedRepository>()

    // если онбординг уже пройден — сразу на логин
    LaunchedEffect(Unit) {
        if (onboardingRepository.isOnboardingCompleted()) {
            navController.navigate(Route.Login) {
                popUpTo(Route.Onboarding) { inclusive = true }
            }
        }
    }

    // куки протухли — триггерим логаут и навигируем на логин
    LaunchedEffect(logoutEventBus) {
        logoutEventBus.logoutEvents.collect {
            navController.navigate(Route.Login) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // валидна ли сессия: если да — сразу идём на главный экран
    LaunchedEffect(Unit) {
        val isValid = runCatching { sessionChecker.isSessionValid() }.getOrDefault(false)
        if (isValid) {
            navController.navigate(Route.Main) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(
                onOnboardingComplete = {
                    scope.launch {
                        onboardingRepository.setOnboardingCompleted(true)
                        navController.navigate(Route.Login) {
                            popUpTo(Route.Onboarding) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Route.Main> {
            MainScreen()
        }
    }
}
