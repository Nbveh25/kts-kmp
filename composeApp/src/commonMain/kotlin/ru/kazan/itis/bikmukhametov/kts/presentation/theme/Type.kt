package ru.kazan.itis.bikmukhametov.kts.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kts.composeapp.generated.resources.Res
import kts.composeapp.generated.resources.roboto_bold
import kts.composeapp.generated.resources.roboto_medium
import kts.composeapp.generated.resources.roboto_regular
import org.jetbrains.compose.resources.Font

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

        //тайтл
        displayLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            letterSpacing = 0.5.sp
        ),

        //жирный обычный текст
        bodyLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            letterSpacing = 0.5.sp
        ),

        //нежирный обычный текст
        bodySmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),

        //медиум текст
        bodyMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )
}