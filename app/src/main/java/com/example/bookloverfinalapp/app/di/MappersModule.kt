package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.mappers.*
import com.example.bookloverfinalapp.app.models.*
import com.example.data.cache.mappers.*
import com.example.data.cache.models.BookCache
import com.example.data.cache.models.BookThatReadCache
import com.example.data.cache.models.ClassCache
import com.example.data.cache.models.UserCache
import com.example.data.cloud.mappers.*
import com.example.data.cloud.models.*
import com.example.data.mappers.*
import com.example.data.models.*
import com.example.domain.Mapper
import com.example.domain.models.*
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
    fun provideBookDbBookMapper(): Mapper<BookData, BookCache> = BookDataToBookDbMapper()


    @Provides
    @Singleton
    fun provideBookDomainToBookMapper(): Mapper<BookDomain, Book> =
        BookDomainToBookMapper()


    @Provides
    @Singleton
    fun provideBookCloudDataMapper(): BookCloudDataMapper = BookCloudDataMapperImpl()

    @Provides
    @Singleton
    fun provideBookDbToDataMapper(): Mapper<BookCache, BookData> = BookDbToDataMapper()


    @Provides
    @Singleton
    fun provideBookDataMapper(): Mapper<BookData, BookDomain> = BookDataToDomainMapper()


    @Provides
    @Singleton
    fun provideBookThatReadDbBookMapper(): Mapper<BookThatReadData, BookThatReadCache> =
        BookThatReadDataToBookDbMapper()


    @Provides
    @Singleton
    fun provideBookThatReadDbToDataMapper(): Mapper<BookThatReadCache, BookThatReadData> =
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
    fun provideAddNewBookMapper(): Mapper<AddNewBookThatReadDomain, AddNewBookThatReadCloud> =
        AddNewBookMapper()


    @Provides
    @Singleton
    fun provideAddBookModelToDomainMapper(): Mapper<AddNewBookModel, AddNewBookThatReadDomain> =
        AddBookThatReadToDomainMapper()

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
    fun provideUserToUserDomainMapper(): Mapper<User, UserDomain> =
        UserToUserDomainMapper()


    @Provides
    @Singleton
    fun provideUserDbToDataMapper(): Mapper<UserCache, StudentData> =
        UserDbToDataMapper()

    @Provides
    @Singleton
    fun provideStudentDataToDomainMapper(): Mapper<StudentData, StudentDomain> =
        StudentDataToDomainMapper()


    @Provides
    @Singleton
    fun provideUserDataToDbMapper(): Mapper<StudentData, UserCache> =
        StudentDataToDbMapper()


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

    @Provides
    @Singleton
    fun provideBookQuestionDataToCloudMapper(): Mapper<AddBookQuestionData, AddBookQuestionCloud> =
        AddBookQuestionDataToCloudMapper()

    @Provides
    @Singleton
    fun provideBookQuestionDomainToDataMapper(): Mapper<AddBookQuestionDomain, AddBookQuestionData> =
        AddBookQuestionDomainToDataMapper()

    @Provides
    @Singleton
    fun provideBookQuestionToDomainMapper(): Mapper<AddBookQuestion, AddBookQuestionDomain> =
        AddBookQuestionToDomainMapper()

    @Provides
    @Singleton
    fun provideClassCloudToDataMapper(): Mapper<ClassCloud, ClassData> =
        ClassCloudToDataMapper()


    @Provides
    @Singleton
    fun provideClassDataToCacheMapper(): Mapper<ClassData, ClassCache> =
        ClassDataToCacheMapper()

    @Provides
    @Singleton
    fun provideClassDataToDomainMapper(): Mapper<ClassData, ClassDomain> =
        ClassDataToDomainMapper()


    @Provides
    @Singleton
    fun provideClassCacheToDataMapper(): Mapper<ClassCache, ClassData> =
        ClassCacheToDataMapper()

    @Provides
    @Singleton
    fun provideClassDomainToClassMapper(): Mapper<ClassDomain, SchoolClass> =
        ClassDomainToClassMapper()


    @Provides
    @Singleton
    fun provideAddBookDomainToDataMapper(): Mapper<AddNewBookDomain, AddNewBookData> =
        AddBookDomainToDataMapper()


    @Provides
    @Singleton
    fun provideAddBookDataMapperToCloudMapper(): Mapper<AddNewBookData, AddNewBookCloud> =
        AddBookDataMapperToCloudMapper()

    @Provides
    @Singleton
    fun provideAddNewBookToDomainMapper(): Mapper<AddNewBook, AddNewBookDomain> =
        AddNewBookToDomainMapper()

    @Provides
    @Singleton
    fun provideUpdateBookDomainToDataMapper(): Mapper<UpdateBookDomain, UpdateBookData> =
        UpdateBookDomainToDataMapper()

    @Provides
    @Singleton
    fun provideUpdateBookDataToCloudMapper(): Mapper<UpdateBookData, UpdateBookCloud> =
        UpdateBookDataToCloudMapper()



    @Provides
    @Singleton
    fun provideUserSaveModelToDomainMapper(): Mapper<UserSaveModel, UserDomain> =
        UserSaveModelToDomainMapper()

    @Provides
    @Singleton
    fun provideUserDomainToSaveModelMapper(): Mapper<UserDomain, UserSaveModel> =
        UserDomainToSaveModelMapper()
}