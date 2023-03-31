package com.example.bookloverfinalapp.app.di

import com.example.data.repository.*
import com.example.data.repository.audio_books.AudioBooksRepositoryImpl
import com.example.data.repository.books.BooksRepositoryImpl
import com.example.data.repository.genres.GenresRepositoryImpl
import com.example.data.repository.questions.QuestionsRepositoryImpl
import com.example.data.repository.saved_books.BookThatReadRepositoryImpl
import com.example.data.repository.stories.StoriesRepositoryImpl
import com.example.data.repository.tasks.TasksRepositoryImpl
import com.example.data.repository.user_statistics.UserStatisticsRepositoryImpl
import com.example.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Binds
    abstract fun bindAudioBooksRepository(
        impl: AudioBooksRepositoryImpl
    ): AudioBooksRepository

    @Binds
    abstract fun bindTasksRepository(
        impl: TasksRepositoryImpl
    ): TasksRepository

    @Binds
    abstract fun bindUserStatisticsRepository(
        impl: UserStatisticsRepositoryImpl
    ): UserStatisticsRepository

    @Binds
    abstract fun bindBookThatReadRepository(
        impl: BookThatReadRepositoryImpl
    ): BookThatReadRepository

    @Binds
    abstract fun bindBooksRepository(
        impl: BooksRepositoryImpl
    ): BooksRepository

    @Binds
    abstract fun bindGenresRepository(
        impl: GenresRepositoryImpl
    ): GenresRepository

    @Binds
    abstract fun bindStoriesRepository(
        impl: StoriesRepositoryImpl
    ): StoriesRepository

    @Binds
    abstract fun bindQuestionsRepository(
        impl: QuestionsRepositoryImpl
    ): QuestionsRepository
}