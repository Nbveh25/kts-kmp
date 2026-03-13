package ru.kazan.itis.bikmukhametov.profile.impl.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.profile.api.repository.ProfileRepository
import ru.kazan.itis.bikmukhametov.profile.api.usecase.GetProfileInfoUseCase
import ru.kazan.itis.bikmukhametov.profile.api.usecase.LogoutUseCase
import ru.kazan.itis.bikmukhametov.profile.impl.data.repository.ProfileRepositoryImpl
import ru.kazan.itis.bikmukhametov.profile.impl.domain.usecase.GetProfileInfoUseCaseImpl
import ru.kazan.itis.bikmukhametov.profile.impl.domain.usecase.LogoutUseCaseImpl
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen.ProfileViewModel

val profileModule = module {
    // data layer
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }

    // domain layer
    single<GetProfileInfoUseCase> { GetProfileInfoUseCaseImpl(get()) }
    single<LogoutUseCase> { LogoutUseCaseImpl(get()) }

    viewModelOf(::ProfileViewModel)
}
