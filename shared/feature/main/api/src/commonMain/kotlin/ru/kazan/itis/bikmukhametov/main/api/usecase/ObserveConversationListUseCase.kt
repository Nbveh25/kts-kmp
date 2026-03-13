package ru.kazan.itis.bikmukhametov.main.api.usecase

import kotlinx.coroutines.flow.Flow
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel

interface ObserveConversationListUseCase {
    operator fun invoke(): Flow<List<ConversationModel>>
}
