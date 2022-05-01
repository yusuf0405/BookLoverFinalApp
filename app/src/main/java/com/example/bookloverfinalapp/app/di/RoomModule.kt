package com.example.bookloverfinalapp.app.di

import android.content.Context
import androidx.room.Room
import com.example.data.data.cache.db.BookDB
import com.example.data.data.cache.db.BooksDao
import com.example.data.data.cache.db.BookThatReadDB
import com.example.data.data.cache.db.BookThatReadDao
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
        "student_books_table"
    ).build()

    @Provides
    @Singleton
    fun provideBookThatReadDao(db: BookThatReadDB): BookThatReadDao =
        db.bookDao()
}