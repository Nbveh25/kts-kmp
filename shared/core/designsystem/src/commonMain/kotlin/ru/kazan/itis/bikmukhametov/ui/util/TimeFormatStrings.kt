package ru.kazan.itis.bikmukhametov.ui.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.Res
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_april
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_august
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_december
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_february
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_january
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_july
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_june
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_march
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_may
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_november
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_october
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.month_september
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_today
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_unknown
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_fri
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_mon
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_sat
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_sun
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_thu
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_tue
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_weekday_wed
import ru.kazan.itis.bikmukhametov.designsystem.generated.resources.time_yesterday

/**
 * Локализованные строки для [formatTimeForUi] и [formatDateLabel].
 */
data class TimeFormatStrings(
    val yesterday: String,
    val today: String,
    /** Индекс 0 = понедельник … 6 = воскресенье (соответствует dayOfWeek 1..7). */
    val weekdayShort: List<String>,
    val unknown: String,
    /** Родительный падеж, индекс 0 = январь … 11 = декабрь. */
    val monthsGenitive: List<String>,
)

@Composable
fun rememberTimeFormatStrings(): TimeFormatStrings {
    val yesterday = stringResource(Res.string.time_yesterday)
    val today = stringResource(Res.string.time_today)
    val unknown = stringResource(Res.string.time_unknown)
    val weekdayShort = listOf(
        stringResource(Res.string.time_weekday_mon),
        stringResource(Res.string.time_weekday_tue),
        stringResource(Res.string.time_weekday_wed),
        stringResource(Res.string.time_weekday_thu),
        stringResource(Res.string.time_weekday_fri),
        stringResource(Res.string.time_weekday_sat),
        stringResource(Res.string.time_weekday_sun),
    )
    val monthsGenitive = listOf(
        stringResource(Res.string.month_january),
        stringResource(Res.string.month_february),
        stringResource(Res.string.month_march),
        stringResource(Res.string.month_april),
        stringResource(Res.string.month_may),
        stringResource(Res.string.month_june),
        stringResource(Res.string.month_july),
        stringResource(Res.string.month_august),
        stringResource(Res.string.month_september),
        stringResource(Res.string.month_october),
        stringResource(Res.string.month_november),
        stringResource(Res.string.month_december),
    )
    return TimeFormatStrings(
        yesterday = yesterday,
        today = today,
        weekdayShort = weekdayShort,
        unknown = unknown,
        monthsGenitive = monthsGenitive,
    )
}
