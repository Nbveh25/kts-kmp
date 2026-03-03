package ru.kazan.itis.bikmukhametov.main.api.usecase

import ru.kazan.itis.bikmukhametov.main.api.model.PostModel

interface GetPostListUseCase {
    suspend operator fun invoke(): List<PostModel>
}

