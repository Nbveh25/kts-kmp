package ru.kazan.itis.bikmukhametov.main.impl.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.PostDataSource
import ru.kazan.itis.bikmukhametov.main.api.repository.PostRepository
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetPostListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.PostDataSourceImpl
import ru.kazan.itis.bikmukhametov.main.impl.data.repository.PostRepositoryImpl
import ru.kazan.itis.bikmukhametov.main.impl.domain.usecase.GetPostListUseCaseImpl
import ru.kazan.itis.bikmukhametov.main.impl.presentation.screen.MainViewModel

/*
* koin-модуль фичи Main
* */
val mainModule = module {

    // Data layer
    single<PostDataSource> { PostDataSourceImpl() }
    single<PostRepository> { PostRepositoryImpl(get()) }

    // Domain Layer
    single<GetPostListUseCase> { GetPostListUseCaseImpl(get()) }

    // Presentation layer
    viewModelOf(::MainViewModel)
}

