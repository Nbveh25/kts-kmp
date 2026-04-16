package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.UserListDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.UserListModel
import ru.kazan.itis.bikmukhametov.main.api.repository.UserListRepository

internal class UserListRepositoryImpl(
    private val dataSource: UserListDataSource,
) : UserListRepository {
    override suspend fun getUserLists(): Result<List<UserListModel>> =
        dataSource.getUserLists()
}
