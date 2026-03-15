package ru.kazan.itis.bikmukhametov.chat.impl.di

import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.chat.api.repository.ChatRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.GetChatMessagesUseCase
import ru.kazan.itis.bikmukhametov.chat.impl.data.repository.ChatRepositoryImpl
import ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase.GetChatMessagesUseCaseImpl

val chatModule = module {
    single<ChatRepository> { ChatRepositoryImpl() }
    single<GetChatMessagesUseCase> { GetChatMessagesUseCaseImpl(get()) }
}
