package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.datasource.InterlocutorDataSource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.repository.InterlocutorRepository
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserChatsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserListsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserPlannedEventsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.usecase.GetUserVarsUseCase
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.datasource.remote.InterlocutorRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.repository.InterlocutorRepositoryImpl
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase.GetUserChatsUseCaseImpl
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase.GetUserListsUseCaseImpl
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase.GetUserPlannedEventsUseCaseImpl
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.domain.usecase.GetUserVarsUseCaseImpl
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen.InterlocutorInfoViewModel

val interlocutorInfoModule = module {

    // Data layer
    factory<InterlocutorDataSource> { InterlocutorRemoteDataSourceImpl(get()) }
    factory<InterlocutorRepository> { InterlocutorRepositoryImpl(get()) }

    // Domain layer
    factory<GetUserVarsUseCase> { GetUserVarsUseCaseImpl(get()) }
    factory<GetUserListsUseCase> { GetUserListsUseCaseImpl(get()) }
    factory<GetUserChatsUseCase> { GetUserChatsUseCaseImpl(get()) }
    factory<GetUserPlannedEventsUseCase> { GetUserPlannedEventsUseCaseImpl(get()) }

    // Presentation layer
    viewModel { (ids: Pair<String, String>) ->
        InterlocutorInfoViewModel(
            chatId = ids.first,
            userId = ids.second,
            getUserVarsUseCase = get(),
            getUserListsUseCase = get(),
            getUserChatsUseCase = get(),
            getUserPlannedEventsUseCase = get(),
        )
    }
}
