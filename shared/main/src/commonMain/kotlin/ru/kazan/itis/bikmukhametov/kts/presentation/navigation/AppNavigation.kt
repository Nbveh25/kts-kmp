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
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.InterlocutorInfoScreen
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

    /*
     * Один проход при старте: если онбординг уже пройден — либо Main (живая сессия), либо Login.
     * Раньше два LaunchedEffect(Unit) гонялись и могли открыть главный экран во время ввода на логине.
     */
    LaunchedEffect(onboardingRepository, sessionChecker, navController) {
        if (!onboardingRepository.isOnboardingCompleted()) return@LaunchedEffect

        val isValid = runCatching { sessionChecker.isSessionValid() }.getOrDefault(false)
        if (isValid) {
            navController.navigate(Route.Main) {
                popUpTo(Route.Onboarding) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.navigate(Route.Login) {
                popUpTo(Route.Onboarding) { inclusive = true }
                launchSingleTop = true
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
                onUserInfoClick = { interlocutorName, channelKind, channelName, chatId, userId ->
                    navController.navigate(
                        Route.InterlocutorInfo(
                            conversationId = chatRoute.conversationId,
                            interlocutorName = interlocutorName,
                            channelKind = channelKind,
                            channelName = channelName,
                            chatId = chatId,
                            userId = userId,
                        ),
                    )
                }
            )
        }

        composable<Route.InterlocutorInfo> { backStackEntry ->
            val infoRoute: Route.InterlocutorInfo = backStackEntry.toRoute()
            InterlocutorInfoScreen(
                conversationId = infoRoute.conversationId,
                interlocutorName = infoRoute.interlocutorName,
                channelKind = infoRoute.channelKind,
                channelName = infoRoute.channelName,
                chatId = infoRoute.chatId,
                userId = infoRoute.userId,
                onClose = { navController.popBackStack() },
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
