package com.example.bookloverfinalapp.app.di

import com.example.data.data.ResourceProvider
import com.example.data.data.cache.db.BooksDao
import com.example.data.data.cache.db.BooksThatReadDao
import com.example.data.data.cache.db.UsersDao
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cache.source.BooksThatReadDataSource
import com.example.data.data.cache.source.UsersCacheDataSource
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.models.BookQuestionCloud
import com.example.data.data.cloud.models.UpdateCloud
import com.example.data.data.cloud.models.UserUpdateCloud
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
import com.example.domain.domain.Mapper
import com.example.domain.models.student.UpdateAnswerDomain
import com.example.domain.models.student.UserUpdateDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideBooksCloudDataSource(
        service: BookService,
        bookCloudMapper: Mapper<BookCloud, BookData>,
        questionCloudMapper: Mapper<BookQuestionCloud, BookQuestionData>,
        resourceProvider: ResourceProvider,
    ): BooksCloudDataSource =
        BooksCloudDataSource.Base(service = service,
            bookCloudMapper = bookCloudMapper,
            questionCloudMapper = questionCloudMapper,
            resourceProvider = resourceProvider)

    @Provides
    @Singleton
    fun provideBookCloudDataSource(
        thatReadService: BookThatReadService, resourceProvider: ResourceProvider,
    ): BooksThatReadCloudDataSource =
        BooksThatReadCloudDataSource.Base(thatReadService = thatReadService,
            resourceProvider = resourceProvider)

    @Provides
    @Singleton
    fun provideUsersCloudDataSource(
        service: UserService,
        userToDataMapper: Mapper<UserUpdateDomain, UserUpdateCloud>,
        updateMapper: Mapper<UpdateCloud, UpdateAnswerDomain>,
        resourceProvider: ResourceProvider,
    ): UsersCloudDataSource = UsersCloudDataSource.Base(service = service,
        userToDataMapper = userToDataMapper,
        updateMapper = updateMapper, resourceProvider = resourceProvider)


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


}