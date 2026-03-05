package ru.kazan.itis.bikmukhametov.impl.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.api.datasource.remote.LoginDataSource
import ru.kazan.itis.bikmukhametov.api.repository.LoginRepository
import ru.kazan.itis.bikmukhametov.api.usecase.LoginUseCase
import ru.kazan.itis.bikmukhametov.impl.data.datasource.remote.LoginDataSourceImpl
import ru.kazan.itis.bikmukhametov.impl.data.repository.LoginRepositoryImpl
import ru.kazan.itis.bikmukhametov.impl.domain.usecase.LoginUseCaseImpl
import ru.kazan.itis.bikmukhametov.impl.presentation.screen.LoginViewModel

/*
 * Koin-модуль фичи Login
 */
val loginModule = module {

    // Data layer
    single<LoginDataSource> { LoginDataSourceImpl() }
    single<LoginRepository> { LoginRepositoryImpl(get()) }

    // Domain layer
    single<LoginUseCase> { LoginUseCaseImpl(get()) }

    // Presentation layer 
    viewModelOf(::LoginViewModel)
}
