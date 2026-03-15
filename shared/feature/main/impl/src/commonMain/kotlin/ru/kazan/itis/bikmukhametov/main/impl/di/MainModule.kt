package ru.kazan.itis.bikmukhametov.main.impl.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.database.room.AppDatabase
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.CabinetDataSource
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ConversationDataSource
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.ProjectDataSource
import ru.kazan.itis.bikmukhametov.main.api.repository.CabinetRepository
import ru.kazan.itis.bikmukhametov.main.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.main.api.repository.ProjectRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetCabinetUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetProjectListUseCase
import ru.kazan.itis.bikmukhametov.main.api.usecase.ObserveConversationListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.local.ConversationLocalDataSource
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet.CabinetDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.conversation.ConversationDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.project.ProjectDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.CabinetRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.ConversationRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.ProjectRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetCabinetUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetConversationListUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetProjectListUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.ObserveConversationListUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainViewModel
import ru.kazan.itis.bikmukhametov.network.space.api.SpaceProvider

val mainModule = module {

    // Data layer — local (DAO из БД — один на приложение)
    single { get<AppDatabase>().conversationDao() }
    factory { ConversationLocalDataSource(get()) }

    // Data layer — remote
    factory<CabinetDataSource> { CabinetDataSourceImpl(get()) }
    factory<ProjectDataSource> { ProjectDataSourceImpl(get()) }
    factory<ConversationDataSource> { ConversationDataSourceImpl(get()) }

    // Data layer — repositories
    factory<CabinetRepository> { CabinetRepositoryImpl(get(), get<SpaceProvider>()) }
    factory<ProjectRepository> { ProjectRepositoryImpl(get(), get<SpaceProvider>()) }
    factory<ConversationRepository> { ConversationRepositoryImpl(get(), get()) }

    // Domain layer
    factory<GetCabinetUseCase> { GetCabinetUseCaseImpl(get()) }
    factory<GetProjectListUseCase> { GetProjectListUseCaseImpl(get()) }
    factory<GetConversationListUseCase> { GetConversationListUseCaseImpl(get()) }
    factory<ObserveConversationListUseCase> { ObserveConversationListUseCaseImpl(get()) }

    // Presentation layer
    viewModelOf(::MainViewModel)
}
