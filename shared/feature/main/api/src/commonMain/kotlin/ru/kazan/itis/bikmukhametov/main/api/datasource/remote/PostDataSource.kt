package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.PostModel

interface PostDataSource {
    suspend fun getPosts(): List<PostModel>
}
