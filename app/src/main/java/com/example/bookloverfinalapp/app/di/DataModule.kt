package com.example.bookloverfinalapp.app.di

import com.example.data.api.KnigolyubApi
import com.example.data.data.cache.db.BooksDao
import com.example.data.data.cache.db.BooksThatReadDao
import com.example.data.data.cache.db.UsersDao
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cache.source.BooksThatReadDataSource
import com.example.data.data.cache.source.UsersCacheDataSource
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.models.BookQuestionCloud
import com.example.data.data.cloud.service.BookService
import com.example.data.data.cloud.service.BookThatReadService
import com.example.data.data.cloud.service.UserService
import com.example.data.data.cloud.source.BooksCloudDataSource
import com.example.data.data.cloud.source.BooksThatReadCloudDataSource
import com.example.data.data.cloud.source.UsersCloudDataSource
import com.example.data.data.models.BookData
import com.example.data.data.models.BookQuestionData
import com.example.data.data.models.BookThatReadData
import com.example.data.data.models.StudentData
import com.example.data.data.repository.*
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.*
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.domain.repository.LoginRepository
import com.example.domain.domain.repository.SchoolRepository
import com.example.domain.domain.repository.UserRepository
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
        cloudDataSource: BooksThatReadCloudDataSource,
        cacheDataSource: BooksThatReadDataSource,
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
    fun provideBookCloudDataSource(thatReadService: BookThatReadService): BooksThatReadCloudDataSource =
        BooksThatReadCloudDataSource.Base(thatReadService = thatReadService)

    @Provides
    @Singleton
    fun provideUsersCloudDataSource(
        service: UserService,
    ): UsersCloudDataSource = UsersCloudDataSource.Base(service = service)


    @Provides
    @Singleton
    fun provideBooksCacheDataSource(
        dao: BooksDao,
        mapper: Mapper<BookData, BookDb>,
    ): BooksCacheDataSource =
        BooksCacheDataSource.Base(bookDao = dao, dataMapper = mapper)

    @Provides
    @Singleton
    fun provideUsersCacheDataSource(
        dao: UsersDao,
        dataMapper: Mapper<StudentData, StudentDb>,
    ): UsersCacheDataSource = UsersCacheDataSource.Base(dao = dao, dataMapper = dataMapper)


    @Provides
    @Singleton
    fun provideBookCacheDataSource(
        dao: BooksThatReadDao,
        mapper: Mapper<BookThatReadData, BookThatReadDb>,
    ): BooksThatReadDataSource =
        BooksThatReadDataSource.Base(mapper = mapper, dao = dao)

    @Provides
    @Singleton
    fun provideSchoolRepository(api: KnigolyubApi): SchoolRepository =
        SchoolRepositoryImpl(api = api)


    @Provides
    @Singleton
    fun provideUserRepository(
        api: KnigolyubApi,
        cloudDataSource: UsersCloudDataSource,
        cacheDataSource: UsersCacheDataSource,
        userDomainMapper: Mapper<StudentData, StudentDomain>,
        userDbMapper: Mapper<StudentDb, StudentData>,
    ): UserRepository =
        UserRepositoryImpl(api = api,
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            userDomainMapper = userDomainMapper,
            userDbMapper = userDbMapper)

}