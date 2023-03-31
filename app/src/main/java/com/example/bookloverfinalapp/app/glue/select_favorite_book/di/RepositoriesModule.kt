package com.example.bookloverfinalapp.app.glue.select_favorite_book.di

import com.example.bookloverfinalapp.app.glue.select_favorite_book.AdapterSelectFavoriteBooksRepository
import com.joseph.select_favorite_book.domain.repositories.SelectFavoriteBooksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindSelectFavoriteBooksRepository(
        impl: AdapterSelectFavoriteBooksRepository
    ): SelectFavoriteBooksRepository
}