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
import com.example.data.cache.source.stories.StoriesCacheDataSource
import com.example.data.cache.source.stories.StoriesCacheDataSourceImpl
import com.example.data.cache.source.tasks.TasksCacheDataSource
import com.example.data.cache.source.tasks.TasksCacheDataSourceImpl
import com.example.data.cache.source.users.UsersCacheDataSource
import com.example.data.cache.source.users.UsersCacheDataSourceImpl
import com.example.data.cloud.source.*
import com.example.data.cloud.source.audio_books.AudioBookCloudDataSource
import com.example.data.cloud.source.audio_books.AudioBookCloudDataSourceImpl
import com.example.data.cloud.source.books.BooksCloudDataSource
import com.example.data.cloud.source.books.BooksCloudDataSourceImpl
import com.example.data.cloud.source.genres.GenresCloudDataSource
import com.example.data.cloud.source.genres.GenresCloudDataSourceImpl
import com.example.data.cloud.source.questions.QuestionsCloudDataSource
import com.example.data.cloud.source.questions.QuestionsCloudDataSourceImpl
import com.example.data.cloud.source.saved_books.BooksThatReadCloudDataSource
import com.example.data.cloud.source.saved_books.BooksThatReadCloudDataSourceImpl
import com.example.data.cloud.source.school_classes.ClassCloudDataSource
import com.example.data.cloud.source.school_classes.ClassCloudDataSourceImpl
import com.example.data.cloud.source.stories.StoriesCloudDataSource
import com.example.data.cloud.source.stories.StoriesCloudDataSourceImpl
import com.example.data.cloud.source.tasks.TasksCloudDataSource
import com.example.data.cloud.source.tasks.TasksCloudDataSourceImpl
import com.example.data.cloud.users.UsersCloudDataSource
import com.example.data.cloud.users.UsersCloudDataSourceImpl
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

    @Binds
    abstract fun bindStoriesCacheDataSource(
        impl: StoriesCacheDataSourceImpl
    ): StoriesCacheDataSource

    @Binds
    abstract fun bindStoriesCloudDataSource(
        impl: StoriesCloudDataSourceImpl
    ): StoriesCloudDataSource

    @Binds
    abstract fun bindQuestionsCloudDataSource(
        impl: QuestionsCloudDataSourceImpl
    ): QuestionsCloudDataSource
}