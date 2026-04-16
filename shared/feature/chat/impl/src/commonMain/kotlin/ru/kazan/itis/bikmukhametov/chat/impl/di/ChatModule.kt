package ru.kazan.itis.bikmukhametov.chat.impl.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlinx.serialization.json.Json
import ru.kazan.itis.bikmukhametov.chat.api.datasource.BotDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ChatWebSocketDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ConversationDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.BlocksDataSource
import ru.kazan.itis.bikmukhametov.chat.api.datasource.ScenariosDataSource
import ru.kazan.itis.bikmukhametov.chat.api.repository.BotRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatWebSocketRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ConversationRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.BlocksRepository
import ru.kazan.itis.bikmukhametov.chat.api.repository.ScenariosRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetBlocksListUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetScenariosListUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetConversationByIdUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.ObserveChatUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.SendMessageUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.UploadChatAttachmentUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StartBotUseCase
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StopBotUseCase
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.bot.BotRemoteDataSource
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.chat.ChatRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.conversation.ConversationRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.blocks.BlocksRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.scenarios.ScenariosRemoteDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket.ChatWebSocketDataSourceImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.BotRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatWebSocketRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ConversationRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.BlocksRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ScenariosRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetBlocksListUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetScenariosListUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetChatMessagesUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetConversationByIdUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.ObserveChatUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.SendMessageUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.UploadChatAttachmentUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.StartBotUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.StopBotUseCaseImpl
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.ChatViewModel

val chatModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }
    }

    // Data layer
    factory<ChatDataSource> { ChatRemoteDataSourceImpl(get(), get()) }
    factory<ChatRepository> { ChatRepositoryImpl(get()) }

    factory<ConversationDataSource> { ConversationRemoteDataSourceImpl(get()) }
    factory<ConversationRepository> { ConversationRepositoryImpl(get()) }

    factory<ScenariosDataSource> { ScenariosRemoteDataSourceImpl(get()) }
    factory<ScenariosRepository> { ScenariosRepositoryImpl(get()) }

    factory<BlocksDataSource> { BlocksRemoteDataSourceImpl(get()) }
    factory<BlocksRepository> { BlocksRepositoryImpl(get()) }

    factory<BotDataSource> { BotRemoteDataSource(get()) }
    factory<BotRepository> { BotRepositoryImpl(get()) }

    factory<ChatWebSocketDataSource> { ChatWebSocketDataSourceImpl(get(), get()) }
    factory<ChatWebSocketRepository> { ChatWebSocketRepositoryImpl(get()) }

    // Domain
    factory<GetScenariosListUseCase> { GetScenariosListUseCaseImpl(get()) }
    factory<GetBlocksListUseCase> { GetBlocksListUseCaseImpl(get()) }
    factory<GetChatMessagesUseCase> { GetChatMessagesUseCaseImpl(get()) }
    factory<GetConversationByIdUseCase> { GetConversationByIdUseCaseImpl(get()) }
    factory<SendMessageUseCase> { SendMessageUseCaseImpl(get()) }
    factory<UploadChatAttachmentUseCase> { UploadChatAttachmentUseCaseImpl(get()) }
    factory<StartBotUseCase> { StartBotUseCaseImpl(get()) }
    factory<StopBotUseCase> { StopBotUseCaseImpl(get()) }
    factory<ObserveChatUseCase> { ObserveChatUseCaseImpl(get()) }

    // Presentation
    viewModelOf(::ChatViewModel)
}
