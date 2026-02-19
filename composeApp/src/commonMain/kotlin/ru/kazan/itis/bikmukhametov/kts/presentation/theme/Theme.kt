package ru.kazan.itis.bikmukhametov.kts.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = KtsViolet,
    onPrimary = Color(0xFFFFFFFF),
    secondary = KtsGreen,
    onSecondary = KtsGraphite,
    background = KtsBgLight,
    surface = KtsSurfaceLight,
    onBackground = KtsTextPrimary,
    onSurface = KtsTextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = KtsViolet,
    onPrimary = Color(0xFFFFFFFF),
    secondary = KtsGreen,
    onSecondary = Color(0xFF000000),
    background = KtsBgDark,
    surface = KtsSurfaceDark,
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF)
)

@Composable
fun KtsMetaclassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = content
    )
}
