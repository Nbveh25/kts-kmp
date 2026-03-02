package ru.kazan.itis.bikmukhametov.main.impl.domain.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.PostModel
import ru.kazan.itis.bikmukhametov.main.api.repository.PostRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetPostListUseCase

internal class GetPostListUseCaseImpl(
    private val postRepository: PostRepository
): GetPostListUseCase {
    
    override suspend fun invoke(): List<PostModel> {
        return postRepository.getPosts()
    }

}