package com.example.bookloverfinalapp.app.glue.stories.di

import com.example.bookloverfinalapp.app.glue.select_favorite_book.AdapterSelectFavoriteBooksRepository
import com.example.bookloverfinalapp.app.glue.stories.AdapterStoriesRepository
import com.joseph.select_favorite_book.domain.repositories.SelectFavoriteBooksRepository
import com.joseph.stories.domain.repositories.StoriesFeatureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindStoriesFeatureRepository(
        impl: AdapterStoriesRepository
    ): StoriesFeatureRepository
}