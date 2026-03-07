package ru.kazan.itis.bikmukhametov.kts

import android.app.Application
import org.koin.android.ext.koin.androidContext
import ru.kazan.itis.bikmukhametov.network.di.platformModules
import ru.kazan.itis.bikmukhametov.kts.presentation.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            config = { androidContext(this@App) },
            additionalModules = platformModules()
        )
    }
}
