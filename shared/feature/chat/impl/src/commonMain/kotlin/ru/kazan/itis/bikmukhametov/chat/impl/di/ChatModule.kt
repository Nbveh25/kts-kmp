package ru.kazan.itis.bikmukhametov.chat.impl.di

import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.ChatDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetChatMessagesUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.ChatViewModel
import org.koin.core.module.dsl.viewModelOf

val chatModule = module {

    // Data layer
    factory<ChatDataSource> { ChatDataSourceImpl(get()) }
    factory<ChatRepository> { ChatRepositoryImpl(get()) }

    // Domain
    factory<GetChatMessagesUseCase> { GetChatMessagesUseCaseImpl(get()) }

    // Presentation
    viewModelOf(::ChatViewModel)
    
}
