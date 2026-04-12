package ru.kazan.itis.bikmukhametov.kts.presentation

import androidx.compose.runtime.Composable
import ru.kazan.itis.bikmukhametov.database.locale.AppLanguage

@Composable
actual fun AppLocaleBridge(
    @Suppress("UNUSED_PARAMETER") appLanguage: AppLanguage,
    content: @Composable () -> Unit,
) {
    content()
}
