package ru.kazan.itis.bikmukhametov.main.impl.data.repository

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.PostDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.PostModel
import ru.kazan.itis.bikmukhametov.main.api.repository.PostRepository

internal class PostRepositoryImpl(
    private val postDataSource: PostDataSource
) : PostRepository {
    override suspend fun getPosts(): List<PostModel> { // TODO() RESULT
        return postDataSource.getPosts()
    }

}
