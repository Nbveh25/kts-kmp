package ru.kazan.itis.bikmukhametov.onboarding.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.onboarding.presentation.component.OnboardingPageScreen
import ru.kazan.itis.bikmukhametov.onboarding.presentation.component.PageIndicator
import ru.kazan.itis.bikmukhametov.onboarding.presentation.model.onboardingPagesUi
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onOnboardingComplete: () -> Unit = {}
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { onboardingPagesUi.size }
    )
    val coroutineScope = rememberCoroutineScope()

    val activeColor by remember {
        derivedStateOf { colors[pagerState.currentPage] }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val ui = onboardingPagesUi[pageIndex]
            val isLastPage = pageIndex == onboardingPagesUi.lastIndex

            OnboardingPageScreen(
                ui = ui,
                onNextClick = {
                    if (isLastPage) {
                        onOnboardingComplete()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pageIndex + 1)
                        }
                    }
                }
            )
        }

        PageIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Spacing.paddingExtraLarge * 4),
            pageCount = onboardingPagesUi.size,
            currentPage = pagerState.currentPage,
            activeColor = activeColor,
            inactiveColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
