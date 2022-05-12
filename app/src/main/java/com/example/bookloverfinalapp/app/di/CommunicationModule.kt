package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.utils.communication.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CommunicationModule {

    @Provides
    fun provideErrorCommunication(): ErrorCommunication = ErrorCommunication.Base()

    @Provides
    fun provideBooksCommunication(): BooksCommunication = BooksCommunication.Base()

    @Provides
    fun provideClassAdapterCommunication(): ClassAdapterCommunication =
        ClassAdapterCommunication.Base()


    @Provides
    fun provideBooksQuestionCommunication(): BooksQuestionCommunication =
        BooksQuestionCommunication.Base()

    @Provides
    fun provideStudentsCommunication(): StudentsCommunication = StudentsCommunication.Base()

    @Provides
    fun provideBookThatReadCommunication(): BooksThatReadCommunication =
        BooksThatReadCommunication.Base()

    @Provides
    fun provideStudentCommunication(): StudentCommunication = StudentCommunication.Base()

    @Provides
    fun provideBooksAdapterCommunication(): BooksAdapterModelCommunication =
        BooksAdapterModelCommunication.Base()

    @Provides
    fun provideBookThatReadAdapterCommunication(): BooksThatReadAdapterCommunication =
        BooksThatReadAdapterCommunication.Base()


    @Provides
    fun provideProgressCommunication(): ProgressCommunication = ProgressCommunication.Base()

    @Provides
    fun provideNavigationCommunication(): NavigationCommunication = NavigationCommunication.Base()

    @Provides
    fun provideProgressDialogCommunication(): ProgressDialogCommunication =
        ProgressDialogCommunication.Base()
}