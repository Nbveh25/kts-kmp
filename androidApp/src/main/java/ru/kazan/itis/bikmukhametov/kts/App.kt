package ru.kazan.itis.bikmukhametov.kts

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import ru.kazan.itis.bikmukhametov.database.di.databasePlatformModules
import ru.kazan.itis.bikmukhametov.kts.presentation.di.initKoin
import ru.kazan.itis.bikmukhametov.network.di.platformModules

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        //if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog(defaultTag = "Smart"))
        //}

        initKoin(
            config = { androidContext(this@App) },
            additionalModules = databasePlatformModules() + platformModules()
        )
    }
}
