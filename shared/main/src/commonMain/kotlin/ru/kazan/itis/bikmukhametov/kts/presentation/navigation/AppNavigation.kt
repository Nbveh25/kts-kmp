package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginScreen
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainScreen
import ru.kazan.itis.bikmukhametov.network.auth.LogoutEventBus
import ru.kazan.itis.bikmukhametov.network.auth.SessionChecker
import ru.kazan.itis.bikmukhametov.onboarding.presentation.screens.OnboardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Route = Route.Login // TODO потом надо запоминать что открывал онбординг
) {
    val logoutEventBus = koinInject<LogoutEventBus>()
    val sessionChecker = koinInject<SessionChecker>()

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
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Onboarding) { inclusive = true }
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
