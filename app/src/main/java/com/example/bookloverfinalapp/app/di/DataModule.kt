package com.example.bookloverfinalapp.app.di

import com.example.data.api.KnigolyubApi
import com.example.data.data.cache.db.BooksDao
import com.example.data.data.cache.db.StudentBooksDao
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cache.source.StudentBookCacheDataSource
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.service.BookService
import com.example.data.data.cloud.service.StudentBookService
import com.example.data.data.cloud.source.BooksCloudDataSource
import com.example.data.data.cloud.source.StudentBookCloudDataSource
import com.example.data.data.models.BookData
import com.example.data.data.models.StudentBookData
import com.example.data.data.repository.BooksRepositoryImpl
import com.example.data.data.repository.StudentBooksRepositoryImpl
import com.example.data.repository.BookRepositoryImpl
import com.example.data.repository.LoginRepositoryImpl
import com.example.data.repository.SchoolRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.domain.repository.StudentBooksRepository
import com.example.domain.repository.BookRepository
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
        bookCloudMapper: Mapper<BookCloud, BookData>,
        bookCashMapper: Mapper<BookDb, BookData>,
        bookDomainMapper: Mapper<BookData, BookDomain>,
    ): BooksRepository =
        BooksRepositoryImpl(cloudDataSource,
            cacheDataSource,
            bookCloudMapper,
            bookCashMapper,
            bookDomainMapper)

    @Provides
    @Singleton
    fun provideBooksCloudDataSource(service: BookService): BooksCloudDataSource =
        BooksCloudDataSource.Base(service = service)

    @Provides
    @Singleton
    fun provideStudentBooksRepository(
        cloudDataSource: StudentBookCloudDataSource,
        cacheDataSource: StudentBookCacheDataSource,
        bookCashMapper: Mapper<StudentBookDb, StudentBookData>,
        bookDomainMapper: Mapper<StudentBookData, StudentBookDomain>,
        addNewBookMapper: Mapper<AddNewBookDomain, AddNewBookCloud>,
    ): StudentBooksRepository = StudentBooksRepositoryImpl(
        cloudDataSource,
        cacheDataSource,
        bookCashMapper,
        bookDomainMapper,
        addNewBookMapper = addNewBookMapper)

    @Provides
    @Singleton
    fun provideStudentBookCloudDataSource(service: StudentBookService): StudentBookCloudDataSource =
        StudentBookCloudDataSource.Base(service = service)


    @Provides
    @Singleton
    fun provideBooksCacheDataSource(
        dao: BooksDao,
        mapper: Mapper<BookData, BookDb>,
    ): BooksCacheDataSource =
        BooksCacheDataSource.Base(bookDao = dao, dataMapper = mapper)


    @Provides
    @Singleton
    fun provideStudentBookCacheDataSource(
        dao: StudentBooksDao,
        mapper: Mapper<StudentBookData, StudentBookDb>,
    ): StudentBookCacheDataSource =
        StudentBookCacheDataSource.Base(mapper = mapper, dao = dao)

    @Provides
    @Singleton
    fun provideSchoolRepository(api: KnigolyubApi): SchoolRepository =
        SchoolRepositoryImpl(api = api)

    @Provides
    @Singleton
    fun provideBookRepository(api: KnigolyubApi): BookRepository =
        BookRepositoryImpl(api = api)

    @Provides
    @Singleton
    fun provideStudentRepository(api: KnigolyubApi): UserRepository =
        UserRepositoryImpl(api = api)

}