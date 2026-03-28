package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel

interface GetConversationListUseCase {
    /**
     * @param limit Количество элементов за раз (например, 20)
     * @param offset Смещение (для offset-пагинации)
     * @param fromId ID последнего элемента (для cursor-пагинации, если нужно)
     */
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
        fromId: String? = null
    ): Result<List<ConversationModel>>
}
