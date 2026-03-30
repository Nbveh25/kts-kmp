package ru.kazan.itis.bikmukhametov.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.Res
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.roboto_bold
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.roboto_medium
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.roboto_regular

@Composable
private fun getRobotoFontFamily() = FontFamily(
    Font(Res.font.roboto_bold, weight = FontWeight.Bold),
    Font(Res.font.roboto_regular, weight = FontWeight.Normal),
    Font(Res.font.roboto_medium, weight = FontWeight.Medium)
)

@Composable
fun getTypography(): Typography {
    val roboto = getRobotoFontFamily()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            letterSpacing = 0.5.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            letterSpacing = 0.5.sp
        ),
        bodySmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )
}
