package com.example.bookloverfinalapp.app.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.bookloverfinalapp.app.utils.communication.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object CommunicationModule {


    @Provides
    fun provideClassesCommunication(): ClassesCommunication = ClassesCommunication.Base()

    @Provides
    fun provideBooksCommunication(): BooksCommunication = BooksCommunication.Base()

    @Provides
    fun provideClassStatisticsCommunication(): ClassStatisticsCommunication =
        ClassStatisticsCommunication.Base()

    @Provides
    fun provideBookThatReadCommunication(): BooksThatReadCommunication =
        BooksThatReadCommunication.Base()

    @Provides
    fun provideItemUiCommunication(): ItemUiCommunication =
        ItemUiCommunication.Base()

    @Provides
    fun provideSchoolsCommunication(): SchoolsCommunication =
        SchoolsCommunication.Base()

    @Provides
    fun provideSchoolsErrorCommunication(): SchoolsErrorCommunication =
        SchoolsErrorCommunication.Base()

    @Provides
    fun provideClassErrorCommunication(): ClassErrorCommunication =
        ClassErrorCommunication.Base()

    @Provides
    fun provideStudentsCommunication(): StudentsCommunication =
        StudentsCommunication.Base()
}