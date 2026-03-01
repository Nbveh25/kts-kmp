package ru.kazan.itis.bikmukhametov.kts

import android.app.Application
import org.koin.android.ext.koin.androidContext
import ru.kazan.itis.bikmukhametov.kts.presentation.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@App)
        }
    }
}