package com.example.bookloverfinalapp.app.di

import com.example.data.cache.source.audio_books.AudioBooksCacheDataSource
import com.example.data.cache.source.audio_books.AudioBooksCacheDataSourceImpl
import com.example.data.cache.source.books.BooksCacheDataSource
import com.example.data.cache.source.books.BooksCacheDataSourceImpl
import com.example.data.cache.source.books_that_read.BooksThatReadCacheDataSource
import com.example.data.cache.source.books_that_read.BooksThatReadCacheDataSourceImpl
import com.example.data.cache.source.classes.ClassCacheDataSource
import com.example.data.cache.source.classes.ClassCacheDataSourceImpl
import com.example.data.cache.source.genres.GenresCacheDataSource
import com.example.data.cache.source.genres.GenresCacheDataSourceImpl
import com.example.data.cache.source.tasks.TasksCacheDataSource
import com.example.data.cache.source.tasks.TasksCacheDataSourceImpl
import com.example.data.cache.source.users.UsersCacheDataSource
import com.example.data.cache.source.users.UsersCacheDataSourceImpl
import com.example.data.cloud.source.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceBindModule {

    @Binds
    abstract fun bindAudioBookCloudDataSource(
        impl: AudioBookCloudDataSourceImpl
    ): AudioBookCloudDataSource

    @Binds
    abstract fun bindAudioBooksCacheDataSource(
        impl: AudioBooksCacheDataSourceImpl
    ): AudioBooksCacheDataSource

    @Binds
    abstract fun bindTaskCloudDataSource(
        impl: TasksCloudDataSourceImpl
    ): TasksCloudDataSource

    @Binds
    abstract fun bindBooksCacheDataSource(
        impl: BooksCacheDataSourceImpl
    ): BooksCacheDataSource

    @Binds
    abstract fun bindBooksThatReadCacheDataSource(
        impl: BooksThatReadCacheDataSourceImpl
    ): BooksThatReadCacheDataSource

    @Binds
    abstract fun bindClassCacheDataSource(
        impl: ClassCacheDataSourceImpl
    ): ClassCacheDataSource

    @Binds
    abstract fun bindUsersCacheDataSource(
        impl: UsersCacheDataSourceImpl
    ): UsersCacheDataSource

    @Binds
    abstract fun bindTasksCacheDataSource(
        impl: TasksCacheDataSourceImpl
    ): TasksCacheDataSource


    @Binds
    abstract fun bindUsersCloudDataSource(
        impl: UsersCloudDataSourceImpl
    ): UsersCloudDataSource

    @Binds
    abstract fun bindBooksThatReadCloudDataSource(
        impl: BooksThatReadCloudDataSourceImpl
    ): BooksThatReadCloudDataSource

    @Binds
    abstract fun bindBooksCloudDataSource(
        impl: BooksCloudDataSourceImpl
    ): BooksCloudDataSource

    @Binds
    abstract fun bindClassCloudDataSource(
        impl: ClassCloudDataSourceImpl
    ): ClassCloudDataSource

    @Binds
    abstract fun bindGenreCloudDataSource(
        impl: GenresCloudDataSourceImpl
    ): GenresCloudDataSource

    @Binds
    abstract fun bindGenresCacheDataSource(
        impl: GenresCacheDataSourceImpl
    ): GenresCacheDataSource


}