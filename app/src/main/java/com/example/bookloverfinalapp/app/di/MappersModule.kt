package com.example.bookloverfinalapp.app.di

import com.example.data.data.cache.mappers.BookDbBookMapper
import com.example.data.data.cache.mappers.BookDbToDataMapper
import com.example.data.data.cache.mappers.StudentBookDbBookMapper
import com.example.data.data.cache.mappers.StudentBookDbToDataMapper
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cloud.mappers.BookCloudDataMapper
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.mappers.BookDataMapper
import com.example.data.data.mappers.StudentBookDataMapper
import com.example.data.data.models.BookData
import com.example.data.data.models.StudentBookData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.models.StudentBookDomain
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.models.StudentBookAdapterModel
import com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.mappers.BookAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.mappers.StudentAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.mappers.StudentBookUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    @Singleton
    fun provideBookDbBookMapper(): Mapper<BookData, BookDb> = BookDbBookMapper()

    @Provides
    @Singleton
    fun provideStudentAdapterModelMapper(): Mapper<StudentBookDomain, StudentBookAdapterModel.Base> =
        StudentAdapterModelMapper()


    @Provides
    @Singleton
    fun provideBookCloudDataMapper(): Mapper<BookCloud, BookData> = BookCloudDataMapper()

    @Provides
    @Singleton
    fun provideBookAdapterModelMapper(): Mapper<BookDomain, BookAdapterModel.Base> =
        BookAdapterModelMapper()


    @Provides
    @Singleton
    fun provideBookDbToDataMapper(): Mapper<BookDb, BookData> = BookDbToDataMapper()


    @Provides
    @Singleton
    fun provideBookDataMapper(): Mapper<BookData, BookDomain> = BookDataMapper()


    @Provides
    @Singleton
    fun provideStudentBookDbBookMapper(): Mapper<StudentBookData, StudentBookDb> =
        StudentBookDbBookMapper()


    @Provides
    @Singleton
    fun provideStudentBookDbToDataMapper(): Mapper<StudentBookDb, StudentBookData> =
        StudentBookDbToDataMapper()


    @Provides
    @Singleton
    fun provideStudentBookUiMapper(): Mapper<StudentBookDomain, StudentBook> =
        StudentBookUiMapper()


    @Provides
    @Singleton
    fun provideStudentBookDataMapper(): Mapper<StudentBookData, StudentBookDomain> =
        StudentBookDataMapper()


//    @Provides
//    @Singleton
//    fun provide(): =


}