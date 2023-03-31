package com.example.bookloverfinalapp.app.glue.stories.di

import com.joseph.stories.domain.usecases.FetchAllStoriesUseCase
import com.joseph.stories.domain.usecases.FetchAllStoriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindFetchAllStoriesUseCase(
        impl: FetchAllStoriesUseCaseImpl
    ): FetchAllStoriesUseCase

}