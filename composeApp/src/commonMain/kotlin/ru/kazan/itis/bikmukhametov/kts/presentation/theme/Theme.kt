package ru.kazan.itis.bikmukhametov.kts.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = KtsViolet,
    onPrimary = KtsOnPrimary,
    secondary = KtsGreen,
    onSecondary = KtsGraphite,
    background = KtsBgLight,
    surface = KtsSurfaceLight,
    onBackground = KtsTextPrimary,
    onSurface = KtsTextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = KtsViolet,
    onPrimary = KtsOnPrimary,
    secondary = KtsGreen,
    onSecondary = KtsOnSecondaryDark,
    background = KtsBgDark,
    surface = KtsSurfaceDark,
    onBackground = KtsOnBackgroundDark,
    onSurface = KtsOnSurfaceDark
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
