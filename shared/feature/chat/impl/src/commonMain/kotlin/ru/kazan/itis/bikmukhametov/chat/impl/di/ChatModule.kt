package ru.kazan.itis.bikmukhametov.chat.impl.di

import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.ChatRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetChatMessagesUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.ChatViewModel
import org.koin.core.module.dsl.viewModelOf
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ConversationDataSource
import ru.kazan.itis.bikmukhametov.chat.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation.ConversationRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ConversationRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetConversationByIdUseCaseImpl

val chatModule = module {

    // Data layer
    factory<ChatDataSource> { ChatRemoteDataSourceImpl(get()) }
    factory<ChatRepository> { ChatRepositoryImpl(get()) }

    factory<ConversationDataSource> { ConversationRemoteDataSourceImpl(get()) }
    factory<ConversationRepository> { ConversationRepositoryImpl(get()) }

    // Domain
    factory<GetChatMessagesUseCase> { GetChatMessagesUseCaseImpl(get()) }
    factory<GetConversationByIdUseCase> { GetConversationByIdUseCaseImpl(get()) }

    // Presentation
    viewModelOf(::ChatViewModel)
    
}
