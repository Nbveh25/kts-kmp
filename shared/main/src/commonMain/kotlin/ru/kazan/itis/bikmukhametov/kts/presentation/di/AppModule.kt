package ru.kazan.itis.bikmukhametov.kts.presentation.di

import ru.kazan.itis.bikmukhametov.impl.di.loginModule
import ru.kazan.itis.bikmukhametov.main.impl.di.mainModule
import ru.kazan.itis.bikmukhametov.network.di.networkModule

fun appModules() = listOf(
    networkModule,  // Сетевой модуль
    loginModule,    // Фича логина
    mainModule,     // Фича main
)
