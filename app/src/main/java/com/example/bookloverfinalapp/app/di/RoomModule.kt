package com.example.bookloverfinalapp.app.di

import android.content.Context
import androidx.room.Room
import com.example.bookloverfinalapp.app.utils.cons.APP_DATABASE_NAME
import com.example.data.cache.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context,
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase): BooksDao = database.getBooksDao()

    @Provides
    @Singleton
    fun provideBookThatReadDao(database: AppDatabase): BooksThatReadDao =
        database.getBooksThatReadDao()


    @Provides
    @Singleton
    fun provideUsersDao(database: AppDatabase): UsersDao = database.getUsersDao()

    @Provides
    @Singleton
    fun provideClassDao(database: AppDatabase): ClassDao = database.getClassesDao()

    @Provides
    @Singleton
    fun provideAudioBooksDao(database: AppDatabase): AudioBooksDao = database.audioBooksDao()

    @Provides
    @Singleton
    fun provideTasksDao(database: AppDatabase): TasksDao = database.tasksDao()

    @Provides
    @Singleton
    fun provideUserStatisticDao(database: AppDatabase): UserStatisticDao =
        database.userStatisticDao()

    @Provides
    @Singleton
    fun provideGenreDao(database: AppDatabase): GenreDao =
        database.genresDao()

    @Provides
    @Singleton
    fun provideStoriesDao(database: AppDatabase): StoriesDao = database.storiesDao()

}