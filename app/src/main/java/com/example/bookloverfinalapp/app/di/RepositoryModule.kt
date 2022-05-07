package com.example.bookloverfinalapp.app.di

import com.example.data.api.KnigolyubApi
import com.example.data.data.ResourceProvider
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cache.source.BooksThatReadDataSource
import com.example.data.data.cache.source.UsersCacheDataSource
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.models.SignUpAnswerCloud
import com.example.data.data.cloud.models.UserCloud
import com.example.data.data.cloud.service.LoginService
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
import com.example.domain.domain.repository.*
import com.example.domain.models.student.PostRequestAnswerDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        service: LoginService,
        signInMapper: Mapper<UserCloud, UserDomain>,
        signUpMapper: Mapper<SignUpAnswerCloud, PostRequestAnswerDomain>,
        resourceProvider: ResourceProvider,
    ): LoginRepository =
        LoginRepositoryImpl(service = service,
            signInMapper = signInMapper,
            signUpMapper = signUpMapper,
            resourceProvider = resourceProvider)

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
    fun provideSchoolRepository(api: KnigolyubApi): SchoolRepository =
        SchoolRepositoryImpl(api = api)


    @Provides
    @Singleton
    fun provideUserRepository(
        cloudDataSource: UsersCloudDataSource,
        cacheDataSource: UsersCacheDataSource,
        userDomainMapper: Mapper<StudentData, StudentDomain>,
        userDbMapper: Mapper<StudentDb, StudentData>,
    ): UserRepository =
        UserRepositoryImpl(
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            userDomainMapper = userDomainMapper,
            userDbMapper = userDbMapper)


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

}