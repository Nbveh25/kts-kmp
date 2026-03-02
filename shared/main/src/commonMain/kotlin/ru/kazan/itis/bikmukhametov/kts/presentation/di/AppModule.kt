package ru.kazan.itis.bikmukhametov.kts.presentation.di

import ru.kazan.itis.bikmukhametov.impl.di.loginModule
import ru.kazan.itis.bikmukhametov.main.impl.di.mainModule

fun appModules() = listOf(
    loginModule,    // Фича логина
    mainModule,     // Фича main
)
