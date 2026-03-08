package ru.kazan.itis.bikmukhametov.main.api.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.model.PostModel
// TODO почему у меня api модули - андроидовскиий
interface PostDataSource {
    suspend fun getPosts(): List<PostModel>
}
