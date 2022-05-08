package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.mappers.*
import com.example.bookloverfinalapp.app.models.*
import com.example.data.data.cache.mappers.*
import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cloud.mappers.BookCloudDataMapper
import com.example.data.data.cloud.mappers.BookQuestionCloudMapper
import com.example.data.data.cloud.mappers.PostRequestAnswerToAnswerMapper
import com.example.data.data.cloud.mappers.UserCloudToUserDomainMapper
import com.example.data.data.cloud.models.*
import com.example.data.data.mappers.*
import com.example.data.data.models.BookData
import com.example.data.data.models.BookQuestionData
import com.example.data.data.models.BookThatReadData
import com.example.data.data.models.StudentData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.*
import com.example.domain.models.student.PostRequestAnswerDomain
import com.example.domain.models.student.UpdateAnswerDomain
import com.example.domain.models.student.UserUpdateDomain
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
        BookThatReadAdapterModelMapper()


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
        BookThatReadUiMapper()


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

    @Provides
    @Singleton
    fun provideUserDbToDataMapper(): Mapper<StudentDb, StudentData> =
        UserDbToDataMapper()

    @Provides
    @Singleton
    fun provideStudentDataToDomainMapper(): Mapper<StudentData, StudentDomain> =
        StudentDataToDomainMapper()


    @Provides
    @Singleton
    fun provideUserDataToDbMapper(): Mapper<StudentData, StudentDb> =
        StudentDataToDbMapper()

    @Provides
    @Singleton
    fun provideStudentAdapterModelMapper(): Mapper<StudentDomain, StudentAdapterModel.Base> =
        StudentAdapterModelMapper()

    @Provides
    @Singleton
    fun provideUserUpdateToCloudMapper(): Mapper<UserUpdateDomain, UserUpdateCloud> =
        UserUpdateToCloudMapper()


    @Provides
    @Singleton
    fun provideUpdateCloudToUpdateMapper(): Mapper<UpdateCloud, UpdateAnswerDomain> =
        UpdateCloudToUpdateMapper()

    @Provides
    @Singleton
    fun provideUserCloudToUserDomainMapper(): Mapper<UserCloud, UserDomain> =
        UserCloudToUserDomainMapper()


    @Provides
    @Singleton
    fun providePostRequestAnswerToAnswerMapper(): Mapper<SignUpAnswerCloud, PostRequestAnswerDomain> =
        PostRequestAnswerToAnswerMapper()

    @Provides
    @Singleton
    fun provideStudentDomainToStudentMapper(): Mapper<StudentDomain, Student> =
        StudentDomainToStudentMapper()


}