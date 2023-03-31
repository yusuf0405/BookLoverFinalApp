package com.example.bookloverfinalapp.app.glue.select_favorite_book.di

import com.joseph.select_favorite_book.domain.usecase.FetchSelectFavoriteBooksUseCase
import com.joseph.select_favorite_book.domain.usecase.FetchSelectFavoriteBooksUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindFetchSelectFavoriteBooksUseCase(
        impl: FetchSelectFavoriteBooksUseCaseImpl
    ): FetchSelectFavoriteBooksUseCase

}