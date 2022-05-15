package com.example.bookloverfinalapp.app.di

import com.example.data.ResourceProvider
import com.example.data.cache.db.BooksDao
import com.example.data.cache.db.BooksThatReadDao
import com.example.data.cache.db.ClassDao
import com.example.data.cache.db.UsersDao
import com.example.data.cache.models.BookDb
import com.example.data.cache.models.BookThatReadDb
import com.example.data.cache.models.ClassCache
import com.example.data.cache.models.StudentDb
import com.example.data.cache.source.BooksCacheDataSource
import com.example.data.cache.source.BooksThatReadDataSource
import com.example.data.cache.source.ClassCacheDataSource
import com.example.data.cache.source.UsersCacheDataSource
import com.example.data.cloud.models.*
import com.example.data.cloud.service.BookService
import com.example.data.cloud.service.BookThatReadService
import com.example.data.cloud.service.ClassService
import com.example.data.cloud.service.UserService
import com.example.data.cloud.source.BooksCloudDataSource
import com.example.data.cloud.source.BooksThatReadCloudDataSource
import com.example.data.cloud.source.ClassCloudDataSource
import com.example.data.cloud.source.UsersCloudDataSource
import com.example.data.models.*
import com.example.domain.Mapper
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserUpdateDomain
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
        addBookCloudMapper: Mapper<AddNewBookData, AddNewBookCloud>,
        questionDataMapper: Mapper<AddBookQuestionData, AddBookQuestionCloud>,
        updateBookCloudMapper: Mapper<UpdateBookData, UpdateBookCloud>,
    ): BooksCloudDataSource =
        BooksCloudDataSource.Base(
            service = service,
            bookCloudMapper = bookCloudMapper,
            questionCloudMapper = questionCloudMapper,
            resourceProvider = resourceProvider,
            questionDataMapper = questionDataMapper,
            addBookCloudMapper = addBookCloudMapper,
            updateBookCloudMapper = updateBookCloudMapper)

    @Provides
    @Singleton
    fun provideBookCloudDataSource(
        thatReadService: BookThatReadService,
        resourceProvider: ResourceProvider,
    ): BooksThatReadCloudDataSource =
        BooksThatReadCloudDataSource.Base(
            thatReadService = thatReadService,
            resourceProvider = resourceProvider)

    @Provides
    @Singleton
    fun provideUsersCloudDataSource(
        service: UserService,
        userToDataMapper: Mapper<UserUpdateDomain, UserUpdateCloud>,
        updateMapper: Mapper<UpdateCloud, UpdateAnswerDomain>,
        resourceProvider: ResourceProvider,
    ): UsersCloudDataSource = UsersCloudDataSource.Base(
        service = service,
        userToDataMapper = userToDataMapper,
        updateMapper = updateMapper, resourceProvider = resourceProvider)


    @Provides
    @Singleton
    fun provideBooksCacheDataSource(
        dao: BooksDao,
        mapper: Mapper<BookData, BookDb>,
    ): BooksCacheDataSource =
        BooksCacheDataSource.Base(
            bookDao = dao,
            dataMapper = mapper)

    @Provides
    @Singleton
    fun provideUsersCacheDataSource(
        dao: UsersDao,
        dataMapper: Mapper<StudentData, StudentDb>,
    ): UsersCacheDataSource = UsersCacheDataSource.Base(
        dao = dao,
        dataMapper = dataMapper)


    @Provides
    @Singleton
    fun provideBookCacheDataSource(
        dao: BooksThatReadDao,
        mapper: Mapper<BookThatReadData, BookThatReadDb>,
    ): BooksThatReadDataSource =
        BooksThatReadDataSource.Base(
            mapper = mapper,
            dao = dao)

    @Provides
    @Singleton
    fun provideClassCacheDataSource(
        dao: ClassDao,
        mapper: Mapper<ClassData, ClassCache>,
    ): ClassCacheDataSource = ClassCacheDataSource.Base(dao = dao, mapper = mapper)

    @Provides
    @Singleton
    fun provideClassCloudDataSource(
        service: ClassService,
        resourceProvider: ResourceProvider,
        mapper: Mapper<ClassCloud, ClassData>,
    ): ClassCloudDataSource = ClassCloudDataSource.Base(service = service,
        resourceProvider = resourceProvider,
        mapper = mapper)

}