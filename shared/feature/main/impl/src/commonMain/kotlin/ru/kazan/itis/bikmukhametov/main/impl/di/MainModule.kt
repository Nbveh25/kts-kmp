package ru.kazan.itis.bikmukhametov.main.impl.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository
import ru.kazan.itis.bikmukhametov.main.api.repository.ProjectRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectUseCase
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet.CabinetDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.ProjectDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.CabinetRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.ProjectRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetCabinetUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetProjectUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainViewModel

/*
* koin-модуль фичи Main
* */
val mainModule = module {

    // Data layer
    single<CabinetDataSource> { CabinetDataSourceImpl(get()) }
    single<CabinetRepository> { CabinetRepositoryImpl(get()) }

    single<ProjectDataSource> { ProjectDataSourceImpl() }
    single<ProjectRepository> { ProjectRepositoryImpl(get()) }

    // Domain Layer
    single<GetCabinetUseCase> { GetCabinetUseCaseImpl(get()) }
    //single<GetProjectUseCase> { GetProjectUseCaseImpl(get()) }

    // Presentation layer
    viewModelOf(::MainViewModel)
}

