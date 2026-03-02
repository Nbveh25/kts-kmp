package ru.kazan.itis.bikmukhametov.main.api.repository

import ru.kazan.itis.bikmukhametov.main.api.model.PostModel

interface PostRepository {
    suspend fun getPosts(): List<PostModel>
}
