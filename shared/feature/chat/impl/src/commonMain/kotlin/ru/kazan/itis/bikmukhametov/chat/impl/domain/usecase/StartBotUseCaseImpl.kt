package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.repository.BotRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StartBotUseCase

class StartBotUseCaseImpl(
    private val botRepository: BotRepository
): StartBotUseCase {
    override suspend fun invoke(conversationId: String): Result<Unit> {
        return botRepository.startBot(conversationId)
    }

}