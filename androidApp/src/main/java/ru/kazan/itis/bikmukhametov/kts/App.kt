package ru.kazan.itis.bikmukhametov.kts

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import ru.kazan.itis.bikmukhametov.network.di.platformModules
import ru.kazan.itis.bikmukhametov.kts.presentation.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        initKoin(
            config = { androidContext(this@App) },
            additionalModules = platformModules()
        )
    }
}
