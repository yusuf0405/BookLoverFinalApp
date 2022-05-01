package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.mappers.*
import com.example.data.data.cache.mappers.BookDataToBookDbMapper
import com.example.data.data.cache.mappers.BookDbToDataMapper
import com.example.data.data.cache.mappers.BookThatReadDataToBookDbMapper
import com.example.data.data.cache.mappers.BookThatReadDbToDataMapper
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cloud.mappers.BookCloudDataMapper
import com.example.data.data.cloud.mappers.BookQuestionCloudMapper
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.models.BookQuestionCloud
import com.example.data.data.mappers.AddNewBookMapper
import com.example.data.data.mappers.BookDataMapper
import com.example.data.data.mappers.BookQuestionDataMapper
import com.example.data.data.mappers.BookThatReadDataMapper
import com.example.data.data.models.BookData
import com.example.data.data.models.BookQuestionData
import com.example.data.data.models.BookThatReadData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.models.BookQuestionDomain
import com.example.domain.models.student.UserDomain
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
    fun provideBookDbBookMapper(): Mapper<BookData, BookDb> = BookDataToBookDbMapper()

    @Provides
    @Singleton
    fun provideBookThatReadAdapterModelMapper(): Mapper<BookThatReadDomain, BookThatReadAdapterModel.Base> =
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
    fun provideBookThatReadDbBookMapper(): Mapper<BookThatReadData, BookThatReadDb> =
        BookThatReadDataToBookDbMapper()


    @Provides
    @Singleton
    fun provideBookThatReadDbToDataMapper(): Mapper<BookThatReadDb, BookThatReadData> =
        BookThatReadDbToDataMapper()


    @Provides
    @Singleton
    fun provideBookThatReadUiMapper(): Mapper<BookThatReadDomain, BookThatRead> =
        StudentBookUiMapper()


    @Provides
    @Singleton
    fun provideBookThatReadDataMapper(): Mapper<BookThatReadData, BookThatReadDomain> =
        BookThatReadDataMapper()


    @Provides
    @Singleton
    fun provideAddNewBookMapper(): Mapper<AddNewBookDomain, AddNewBookCloud> = AddNewBookMapper()


    @Provides
    @Singleton
    fun provideAddBookModelToDomainMapper(): Mapper<AddNewBookModel, AddNewBookDomain> =
        AddBookModelToDomainMapper()

    @Provides
    @Singleton
    fun provideBookQuestionCloudMapper(): Mapper<BookQuestionCloud, BookQuestionData> =
        BookQuestionCloudMapper()

    @Provides
    @Singleton
    fun provideBookQuestionDataMapper(): Mapper<BookQuestionData, BookQuestionDomain> =
        BookQuestionDataMapper()


    @Provides
    @Singleton
    fun provideBookQuestionsDomainMapper(): Mapper<BookQuestionDomain, BookQuestion> =
        BookQuestionsDomainMapper()

    @Provides
    @Singleton
    fun provideUserDomainToUserMapper(): Mapper<UserDomain, User> =
        UserDomainToUserMapper()


}