package ru.kazan.itis.bikmukhametov.kts.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.kazan.itis.bikmukhametov.kts.presentation.navigation.AppNavigation
import ru.kazan.itis.bikmukhametov.theme.KtsMetaclassTheme

@Composable
fun App() {
    KtsMetaclassTheme {
        Surface {
            val navController = rememberNavController()
            AppNavigation(navController = navController)
        }
    }
}
