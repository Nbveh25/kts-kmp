package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginScreen
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainScreen
import ru.kazan.itis.bikmukhametov.onboarding.presentation.screens.OnboardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Route = Route.Main // TODO()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(Route.Login)
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
            MainScreen(
               
            )
        }
    }
}
