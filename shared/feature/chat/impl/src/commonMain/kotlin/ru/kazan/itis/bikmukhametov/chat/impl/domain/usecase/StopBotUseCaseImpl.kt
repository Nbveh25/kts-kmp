package ru.kazan.itis.bikmukhametov.chat.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.chat.api.repository.BotRepository
import ru.kazan.itis.bikmukhametov.chat.api.usecase.StopBotUseCase

class StopBotUseCaseImpl(
    private val botRepository: BotRepository
) : StopBotUseCase {

    override suspend fun invoke(conversationId: String): Result<Unit> {
        return botRepository.stopBot(conversationId)

    }

}
