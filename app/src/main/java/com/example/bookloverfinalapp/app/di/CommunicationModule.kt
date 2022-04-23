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
    fun provideNetworkErrorCommunication(): NetworkErrorCommunication =
        NetworkErrorCommunication.Base()

    @Provides
    fun provideBooksCommunication(): BooksCommunication =
        BooksCommunication.Base()

    @Provides
    fun provideStudentBooksCommunication(): StudentBooksCommunication =
        StudentBooksCommunication.Base()


    @Provides
    fun provideProgressCommunication(): ProgressCommunication = ProgressCommunication.Base()

    @Provides
    fun provideNavigationCommunication(): NavigationCommunication = NavigationCommunication.Base()

    @Provides
    fun provideProgressDialogCommunication(): ProgressDialogCommunication =
        ProgressDialogCommunication.Base()
}