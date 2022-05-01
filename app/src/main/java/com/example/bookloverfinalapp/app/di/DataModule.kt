package com.example.bookloverfinalapp.app.di

import com.example.data.api.KnigolyubApi
import com.example.data.data.cache.db.BookThatReadDao
import com.example.data.data.cache.db.BooksDao
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.source.BookThatReadDataSource
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.models.BookQuestionCloud
import com.example.data.data.cloud.service.BookService
import com.example.data.data.cloud.service.BookThatReadService
import com.example.data.data.cloud.source.BookThatReadCloudDataSource
import com.example.data.data.cloud.source.BooksCloudDataSource
import com.example.data.data.models.BookData
import com.example.data.data.models.BookQuestionData
import com.example.data.data.models.BookThatReadData
import com.example.data.data.repository.*
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.models.BookQuestionDomain
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.SchoolRepository
import com.example.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideLoginRepository(api: KnigolyubApi): LoginRepository =
        LoginRepositoryImpl(api = api)

    @Provides
    @Singleton
    fun provideBooksRepository(
        cloudDataSource: BooksCloudDataSource,
        cacheDataSource: BooksCacheDataSource,
        bookCashMapper: Mapper<BookDb, BookData>,
        bookDomainMapper: Mapper<BookData, BookDomain>,
        questionsMapper: Mapper<BookQuestionData, BookQuestionDomain>,
    ): BooksRepository =
        BooksRepositoryImpl(
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            bookCashMapper = bookCashMapper,
            bookDomainMapper = bookDomainMapper,
            questionsMapper = questionsMapper)

    @Provides
    @Singleton
    fun provideBooksCloudDataSource(
        service: BookService,
        bookCloudMapper: Mapper<BookCloud, BookData>,
        questionCloudMapper: Mapper<BookQuestionCloud, BookQuestionData>,
    ): BooksCloudDataSource =
        BooksCloudDataSource.Base(service = service,
            bookCloudMapper = bookCloudMapper,
            questionCloudMapper = questionCloudMapper)

    @Provides
    @Singleton
    fun provideBookThatReadRepository(
        cloudDataSource: BookThatReadCloudDataSource,
        cacheDataSource: BookThatReadDataSource,
        bookCashMapper: Mapper<BookThatReadDb, BookThatReadData>,
        bookDomainMapper: Mapper<BookThatReadData, BookThatReadDomain>,
        addNewBookMapper: Mapper<AddNewBookDomain, AddNewBookCloud>,
    ): BookThatReadRepository = BookThatReadRepositoryImpl(
        cloudDataSource = cloudDataSource,
        cacheDataSource = cacheDataSource,
        bookCashMapper = bookCashMapper,
        bookDomainMapper = bookDomainMapper,
        addNewBookMapper = addNewBookMapper)

    @Provides
    @Singleton
    fun provideBookCloudDataSource(thatReadService: BookThatReadService): BookThatReadCloudDataSource =
        BookThatReadCloudDataSource.Base(thatReadService = thatReadService)


    @Provides
    @Singleton
    fun provideBooksCacheDataSource(
        dao: BooksDao,
        mapper: Mapper<BookData, BookDb>,
    ): BooksCacheDataSource =
        BooksCacheDataSource.Base(bookDao = dao, dataMapper = mapper)


    @Provides
    @Singleton
    fun provideBookCacheDataSource(
        dao: BookThatReadDao,
        mapper: Mapper<BookThatReadData, BookThatReadDb>,
    ): BookThatReadDataSource =
        BookThatReadDataSource.Base(mapper = mapper, dao = dao)

    @Provides
    @Singleton
    fun provideSchoolRepository(api: KnigolyubApi): SchoolRepository =
        SchoolRepositoryImpl(api = api)


    @Provides
    @Singleton
    fun provideStudentRepository(api: KnigolyubApi): UserRepository =
        UserRepositoryImpl(api = api)

}