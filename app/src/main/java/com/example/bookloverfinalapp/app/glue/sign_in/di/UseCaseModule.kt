package com.example.bookloverfinalapp.app.glue.sign_in.di

import com.joseph.sign_in.SignInUseCase
import com.joseph.sign_in.SignInUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bind(
        impl: SignInUseCaseImpl
    ): SignInUseCase
}