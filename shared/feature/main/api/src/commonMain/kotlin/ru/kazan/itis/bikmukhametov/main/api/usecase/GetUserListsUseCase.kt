package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel

interface GetUserListsUseCase {
    suspend operator fun invoke(): Result<List<UserListModel>>
}
