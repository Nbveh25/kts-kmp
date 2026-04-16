package ru.kazan.itis.bikmukhametov.kts.presentation

import androidx.compose.runtime.Composable
import ru.kazan.itis.bikmukhametov.database.locale.AppLanguage

/** Подставляет локаль для Compose (в т.ч. [org.jetbrains.compose.resources.stringResource]) на Android. */
@Composable
expect fun AppLocaleBridge(appLanguage: AppLanguage, content: @Composable () -> Unit)
