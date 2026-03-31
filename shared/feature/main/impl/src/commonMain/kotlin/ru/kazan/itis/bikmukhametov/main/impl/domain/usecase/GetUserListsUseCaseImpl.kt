package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel
import ru.kazan.itis.bikmukhametov.main.api.repository.UserListRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetUserListsUseCase

internal class GetUserListsUseCaseImpl(
    private val repository: UserListRepository,
) : GetUserListsUseCase {
    override suspend fun invoke(): Result<List<UserListModel>> = repository.getUserLists()
}
