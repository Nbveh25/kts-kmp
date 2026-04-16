package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel

interface UserListDataSource {
    suspend fun getUserLists(): Result<List<UserListModel>>
}
