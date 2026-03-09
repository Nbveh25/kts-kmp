package ru.kazan.itis.bikmukhametov.kts.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ru.kazan.itis.bikmukhametov.kts.presentation.navigation.AppNavigation
import ru.kazan.itis.bikmukhametov.theme.KtsMetaclassTheme

@Composable
fun App() {
    KtsMetaclassTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
