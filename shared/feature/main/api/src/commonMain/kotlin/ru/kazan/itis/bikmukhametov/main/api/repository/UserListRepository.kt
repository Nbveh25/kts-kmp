package ru.kazan.itis.bikmukhametov.main.api.repository

import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel

interface UserListRepository {
    suspend fun getUserLists(): Result<List<UserListModel>>
}
