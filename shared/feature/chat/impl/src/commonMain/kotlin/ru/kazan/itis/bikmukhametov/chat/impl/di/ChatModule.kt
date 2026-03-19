package ru.kazan.itis.bikmukhametov.chat.impl.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.chat.api.datasource.BotDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatWebSocketDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ConversationDataSource
import ru.kazan.itis.bikmukhametov.chat.api.repository.BotRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatWebSocketRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.ObserveChatUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.SendMessageUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StartBotUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StopBotUseCase
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.bot.BotRemoteDataSource
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.ChatRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation.ConversationRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket.ChatWebSocketDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.BotRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatWebSocketRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ConversationRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetChatMessagesUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetConversationByIdUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.ObserveChatUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.SendMessageUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.StartBotUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.StopBotUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.ChatViewModel

val chatModule = module {

    // Data layer
    factory<ChatDataSource> { ChatRemoteDataSourceImpl(get()) }
    factory<ChatRepository> { ChatRepositoryImpl(get()) }

    factory<ConversationDataSource> { ConversationRemoteDataSourceImpl(get()) }
    factory<ConversationRepository> { ConversationRepositoryImpl(get()) }

    factory<BotDataSource> { BotRemoteDataSource(get()) }
    factory<BotRepository> { BotRepositoryImpl(get()) }

    factory<ChatWebSocketDataSource> { ChatWebSocketDataSourceImpl(get()) }
    factory<ChatWebSocketRepository> { ChatWebSocketRepositoryImpl(get()) }

    // Domain
    factory<GetChatMessagesUseCase> { GetChatMessagesUseCaseImpl(get()) }
    factory<GetConversationByIdUseCase> { GetConversationByIdUseCaseImpl(get()) }
    factory<SendMessageUseCase> { SendMessageUseCaseImpl(get()) }
    factory<StartBotUseCase> { StartBotUseCaseImpl(get()) }
    factory<StopBotUseCase> { StopBotUseCaseImpl(get()) }
    factory<ObserveChatUseCase> { ObserveChatUseCaseImpl(get()) }

    // Presentation
    viewModelOf(::ChatViewModel)
}
