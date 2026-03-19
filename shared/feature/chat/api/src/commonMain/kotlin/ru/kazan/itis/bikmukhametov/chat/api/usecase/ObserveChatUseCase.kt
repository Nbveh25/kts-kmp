package ru.kazan.itis.bikmukhametov.chat.api.usecase

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel

interface ObserveChatUseCase {
    operator fun invoke(conversationId: String): Flow<ChatMessageModel>
}
