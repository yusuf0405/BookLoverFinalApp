package com.example.bookloverfinalapp.app.di

import android.content.Context
import com.example.bookloverfinalapp.app.utils.Dispatchers
import com.example.data.ResourceProvider
import com.example.domain.interactor.GetCurrentUserUseCase
import com.example.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideDispatchers(): Dispatchers = Dispatchers.Base()

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider =
        ResourceProvider.Base(context = context)

    @Provides
    fun provideGetCurrentUserUseCase(repository: UserRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(repository = repository)
}