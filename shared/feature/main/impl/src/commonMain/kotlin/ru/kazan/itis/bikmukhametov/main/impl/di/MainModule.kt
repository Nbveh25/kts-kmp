package ru.kazan.itis.bikmukhametov.main.impl.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ConversationDataSource
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository
import ru.kazan.itis.bikmukhametov.main.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.main.api.repository.ProjectRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet.CabinetDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.conversation.ConversationDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project.ProjectDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.CabinetRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.ConversationRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.ProjectRepositoryImpl
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetCabinetUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetConversationListUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetProjectListUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainViewModel

/*
* koin-модуль фичи Main
* */
val mainModule = module {

    // Data layer
    single<CabinetDataSource> { CabinetDataSourceImpl(get()) }
    single<CabinetRepository> { CabinetRepositoryImpl(get(), get<SpaceProvider>()) }

    single<ProjectDataSource> { ProjectDataSourceImpl(get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get<SpaceProvider>()) }

    single<ConversationDataSource> { ConversationDataSourceImpl(get()) }
    single<ConversationRepository> { ConversationRepositoryImpl(get()) }
    
    // Domain Layer
    single<GetCabinetUseCase> { GetCabinetUseCaseImpl(get()) }
    single<GetProjectListUseCase> { GetProjectListUseCaseImpl(get()) }
    single<GetConversationListUseCase> { GetConversationListUseCaseImpl(get()) }

    // Presentation layer
    viewModelOf(::MainViewModel)
}

