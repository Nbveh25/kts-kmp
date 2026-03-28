package ru.kazan.itis.bikmukhametov.kts.presentation.di

import org.koin.core.module.Module
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: KoinAppDeclaration? = null,
    additionalModules: List<Module> = emptyList()
) {
    startKoin {
        config?.invoke(this)
        modules(appModules() + additionalModules)
    }
}
