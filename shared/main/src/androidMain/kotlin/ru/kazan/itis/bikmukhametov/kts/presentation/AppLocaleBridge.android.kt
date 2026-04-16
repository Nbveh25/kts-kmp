package ru.kazan.itis.bikmukhametov.kts.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import ru.kazan.itis.bikmukhametov.database.locale.AppLanguage
import java.util.Locale

@Composable
actual fun AppLocaleBridge(appLanguage: AppLanguage, content: @Composable () -> Unit) {
    // stringResource() в CMP смотрит на androidx.compose.ui.text.intl.Locale.current,
    // который на Android привязан к JVM Locale / LocaleList, а не только к LocalConfiguration.
    val javaLocale = remember(appLanguage) {
        Locale.forLanguageTag(appLanguage.tag).also { locale ->
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList.setDefault(LocaleList(locale))
            }
        }
    }

    val baseContext = LocalContext.current
    val localizedContext = remember(javaLocale, baseContext) {
        val config = Configuration(baseContext.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(javaLocale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = javaLocale
        }
        baseContext.createConfigurationContext(config)
    }
    val localizedConfiguration = remember(localizedContext) {
        Configuration(localizedContext.resources.configuration)
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedConfiguration,
    ) {
        key(appLanguage) {
            content()
        }
    }
}
