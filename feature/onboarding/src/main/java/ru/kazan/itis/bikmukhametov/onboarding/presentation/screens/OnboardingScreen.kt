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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.onboarding.presentation.component.FirstScreen
import ru.kazan.itis.bikmukhametov.onboarding.presentation.component.PageIndicator
import ru.kazan.itis.bikmukhametov.onboarding.presentation.component.SecondScreen
import ru.kazan.itis.bikmukhametov.onboarding.presentation.component.ThirdScreen

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onOnboardingComplete: () -> Unit = {}
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.secondary
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 3 }
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
        ) { page ->
            when (page) {
                0 -> FirstScreen(
                    onNextClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )

                1 -> SecondScreen(
                    onNextClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }
                )

                2 -> ThirdScreen(
                    onNextClick = onOnboardingComplete
                )
            }
        }

        PageIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 128.dp),
            pageCount = 3,
            currentPage = pagerState.currentPage,
            activeColor = activeColor,
            inactiveColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

