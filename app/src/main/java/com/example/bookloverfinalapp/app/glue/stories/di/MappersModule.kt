package com.example.bookloverfinalapp.app.glue.stories.di

import com.joseph.common.Mapper
import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import com.joseph.select_favorite_book.presentation.adapter.models.SelectFavoriteBookItem
import com.joseph.select_favorite_book.presentation.mappers.SelectBookToAdapterItemMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {


}