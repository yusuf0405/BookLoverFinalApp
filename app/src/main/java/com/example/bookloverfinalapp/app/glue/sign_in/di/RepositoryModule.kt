package com.example.bookloverfinalapp.app.glue.sign_in.di

import com.example.bookloverfinalapp.app.glue.sign_in.AdapterSignInModuleRepository
import com.joseph.sign_in.SignInModuleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bind(
        impl: AdapterSignInModuleRepository
    ): SignInModuleRepository
}