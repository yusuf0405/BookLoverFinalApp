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
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME).build()

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


}