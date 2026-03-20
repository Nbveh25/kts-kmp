package ru.kazan.itis.bikmukhametov.kts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.compose.koinInject
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.ChatScreen
import ru.kazan.itis.bikmukhametov.database.onboarding.OnboardingCompletedRepository
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginScreen
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainScreen
import ru.kazan.itis.bikmukhametov.network.auth.logout.LogoutEventBus
import ru.kazan.itis.bikmukhametov.network.auth.session.SessionChecker
import ru.kazan.itis.bikmukhametov.onboarding.presentation.screens.OnboardingScreen
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen.ProfileScreen

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
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
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
            MainScreen(
                onProfileClick = {
                    navController.navigate(Route.Profile)
                },
                onChatClick = { conversationId ->
                    navController.navigate(Route.Chat(conversationId = conversationId))
                }
            )
        }

        composable<Route.Chat> { backStackEntry ->
            val chatRoute: Route.Chat = backStackEntry.toRoute()
            ChatScreen(
                conversationId = chatRoute.conversationId,
                onBack = { navController.popBackStack() },
                onUserInfoClick = {
                    /* TODO: экран информации о пользователе */
                }
            )
        }

        composable<Route.Profile> {
            ProfileScreen(
                onChatsClick = {
                    navController.navigate(Route.Main)
                }
            )
        }
    }
}
