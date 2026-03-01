package ru.kazan.itis.bikmukhametov.impl.di

import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginViewModel

/**
 * Koin-модуль фичи Login
 */
val loginModule = module {
    single { LoginViewModel() }
}
