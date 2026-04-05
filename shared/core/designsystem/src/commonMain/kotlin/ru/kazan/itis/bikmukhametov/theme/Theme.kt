package ru.kazan.itis.bikmukhametov.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Обновленные цветовые схемы на основе Smartbot Pro (Color.kt)
private val LightColorScheme = lightColorScheme(
    primary = SmartbotBlue,
    onPrimary = KtsOnPrimary,
    primaryContainer = SmartbotBlueLight,
    onPrimaryContainer = SmartbotBlue,

    secondary = SmartbotSuccess,
    onSecondary = Color.White,

    tertiary = SmartbotSuccess,
    onTertiary = Color.White,

    background = KtsBgLight,
    onBackground = KtsTextPrimary,

    surface = KtsSurfaceLight,
    onSurface = KtsTextPrimary,
    surfaceVariant = SmartbotBlueLight.copy(alpha = 0.5f),
    onSurfaceVariant = KtsTextSecondary,
    outline = KtsTextSecondary.copy(alpha = 0.3f),
    outlineVariant = KtsFieldBorder.copy(alpha = 0.55f),

    error = SmartbotAmber,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = SmartbotBlue,
    onPrimary = KtsOnPrimary,
    primaryContainer = KtsSurfaceDark,
    onPrimaryContainer = SmartbotBlueLight,

    secondary = SmartbotSuccess,
    onSecondary = KtsOnSecondaryDark,

    tertiary = SmartbotSuccess,
    onTertiary = KtsOnPrimary,

    background = KtsBgDark,
    onBackground = KtsOnBackgroundDark,

    surface = KtsSurfaceDark,
    onSurface = KtsOnSurfaceDark,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = KtsOnSurfaceDark.copy(alpha = 0.8f),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF475569),

    error = SmartbotAmber,
    onError = Color.White
)

@Composable
fun KtsMetaclassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Здесь можно добавить проверку на динамические цвета (Android 12+),
    // но для брендированного приложения лучше оставить фиксированную схему
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        // Убедитесь, что getTypography() определен в вашем проекте
        typography = getTypography(),
        content = content
    )
}
