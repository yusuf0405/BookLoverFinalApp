package com.example.bookloverfinalapp.app.glue.profile.di

import com.example.bookloverfinalapp.app.glue.profile.usecase.FetchLoginOutDialogUseCaseImpl
import com.example.bookloverfinalapp.app.glue.profile.usecase.FetchSettingDialogUseCaseImpl
import com.joseph.profile.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindFetchUserInfoUseCase(
        impl: FetchUserInfoUseCaseImpl
    ): FetchUserInfoUseCase

    @Binds
    abstract fun bindFetchSettingDialogUseCase(
        impl: FetchSettingDialogUseCaseImpl
    ): FetchSettingDialogUseCase

    @Binds
    abstract fun bindFetchLoginOutDialogUseCase(
        impl: FetchLoginOutDialogUseCaseImpl
    ): FetchLoginOutDialogUseCase

    @Binds
    abstract fun bindEmailValidatorUseCase(
        impl: EmailValidatorUseCaseImpl
    ): EmailValidatorUseCase


    @Binds
    abstract fun bindNameValidatorUseCase(
        impl: NameValidatorUseCaseImpl
    ): NameValidatorUseCase

    @Binds
    abstract fun bindLastNameValidatorUseCase(
        impl: LastNameValidatorUseCaseImpl
    ): LastNameValidatorUseCase

    @Binds
    abstract fun bindPasswordValidatorUseCase(
        impl: PasswordValidatorUseCaseImpl
    ): PasswordValidatorUseCase

}