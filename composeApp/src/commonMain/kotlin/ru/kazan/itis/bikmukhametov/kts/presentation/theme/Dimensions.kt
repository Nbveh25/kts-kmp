package ru.kazan.itis.bikmukhametov.kts.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Отступы экрана (dimen: horizontal_screen_padding, vertical_screen_padding) */
object Spacing {
    val horizontalScreenPadding: Dp = 16.dp
    val verticalScreenPadding: Dp = 16.dp

    /** Отступы компонентов (dimen: padding_*) */
    val paddingExtraSmall: Dp = 4.dp
    val paddingSmall: Dp = 8.dp
    val paddingMedium: Dp = 16.dp
    val paddingLarge: Dp = 24.dp
    val paddingExtraLarge: Dp = 48.dp
}

/** Тени и возвышение (dimen: *_elevation*) */
object Elevation {
    val cardShadowElevationLarge: Dp = 32.dp
    val cardShadowElevationMedium: Dp = 8.dp
    val buttonElevation: Dp = 2.dp
}

/** Радиусы скругления (dimen: corner_shape_*) */
object CornerShape {
    val cornerShapeSmall: Dp = 8.dp
    val cornerShapeMedium: Dp = 12.dp
    val cornerShapeLarge: Dp = 24.dp
    val cornerShapeExtraLarge: Dp = 48.dp
}

/** Фиксированные размеры UI-элементов (не из dimen) */
object Dimensions {
    val buttonHeight: Dp = 56.dp
    val iconSize: Dp = 20.dp
    val iconButtonSize: Dp = 24.dp
    val onboardingImageSize: Dp = 400.dp
}
