package com.example.bookloverfinalapp.app.glue.profile.di

import com.example.bookloverfinalapp.app.glue.profile.AdapterUserInfoRepository
import com.joseph.profile.domain.repositories.UserInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindUserInfoRepository(
        impl: AdapterUserInfoRepository
    ): UserInfoRepository
}