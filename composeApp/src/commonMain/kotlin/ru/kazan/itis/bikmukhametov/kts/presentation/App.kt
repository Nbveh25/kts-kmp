package ru.kazan.itis.bikmukhametov.kts.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.kazan.itis.bikmukhametov.kts.presentation.navigation.AppNavigation
import ru.kazan.itis.bikmukhametov.kts.presentation.theme.KtsMetaclassTheme

@Composable
@Preview
fun App() {
    KtsMetaclassTheme {
        Surface {
            val navController: NavHostController = rememberNavController()
            AppNavigation(navController = navController)
        }
    }
}
