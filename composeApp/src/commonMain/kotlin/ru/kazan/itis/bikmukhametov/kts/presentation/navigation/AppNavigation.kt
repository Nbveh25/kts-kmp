package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kazan.itis.bikmukhametov.kts.presentation.screens.LoginScreen
import ru.kazan.itis.bikmukhametov.kts.presentation.screens.OnboardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: Any = Route.Onboarding
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(Route.Login)
                }
            )
        }
        composable<Route.Login> {
            LoginScreen()
        }

    }
}
