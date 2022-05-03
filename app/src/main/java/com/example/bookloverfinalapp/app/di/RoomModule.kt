package com.example.bookloverfinalapp.app.di

import android.content.Context
import androidx.room.Room
import com.example.data.data.cache.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideBookDB(
        @ApplicationContext app: Context,
    ): BookDB = Room.databaseBuilder(
        app,
        BookDB::class.java,
        "books_table"
    ).build()

    @Provides
    @Singleton
    fun provideBookDao(db: BookDB): BooksDao =
        db.bookDao()

    @Provides
    @Singleton
    fun provideBookThatReadDB(
        @ApplicationContext app: Context,
    ): BookThatReadDB = Room.databaseBuilder(
        app,
        BookThatReadDB::class.java,
        "books_that_read_table"
    ).build()

    @Provides
    @Singleton
    fun provideBookThatReadDao(db: BookThatReadDB): BooksThatReadDao =
        db.bookDao()


    @Provides
    @Singleton
    fun provideUsersDB(
        @ApplicationContext app: Context,
    ): UsersDB = Room.databaseBuilder(
        app,
        UsersDB::class.java,
        "users_books_table"
    ).build()

    @Provides
    @Singleton
    fun provideUsersDao(db: UsersDB): UsersDao =
        db.usersDao()
}