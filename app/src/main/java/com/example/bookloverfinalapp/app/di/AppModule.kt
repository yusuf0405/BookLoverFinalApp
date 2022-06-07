package com.example.bookloverfinalapp.app.di

import android.content.Context
import com.example.bookloverfinalapp.app.utils.communication.ErrorCommunication
import com.example.bookloverfinalapp.app.utils.communication.NavigationCommunication
import com.example.bookloverfinalapp.app.utils.communication.ProgressCommunication
import com.example.bookloverfinalapp.app.utils.communication.ProgressDialogCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.Dispatchers
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.data.ResourceProvider
import com.example.domain.interactor.*
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.ClassRepository
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
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProvider.Base()


    @Provides
    @Singleton
    fun provideProgressCommunication(): ProgressCommunication = ProgressCommunication.Base()

    @Provides
    @Singleton
    fun provideNavigationCommunication(): NavigationCommunication = NavigationCommunication.Base()

    @Provides
    @Singleton
    fun provideProgressDialogCommunication(): ProgressDialogCommunication =
        ProgressDialogCommunication.Base()

    @Provides
    fun provideErrorCommunication(): ErrorCommunication = ErrorCommunication.Base()

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider =
        ResourceProvider.Base(context = context)

    @Provides
    fun provideGetCurrentUserUseCase(repository: UserRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(repository = repository)


    @Provides
    fun provideClearClassCacheUseCase(repository: ClassRepository): ClearClassCacheUseCase =
        ClearClassCacheUseCase(repository = repository)

    @Provides
    fun provideClearStudentsCacheUseCase(repository: UserRepository): ClearStudentsCacheUseCase =
        ClearStudentsCacheUseCase(repository = repository)

    @Provides
    fun provideClearBooksThatReadCacheUseCase(repository: BookThatReadRepository): ClearBooksThatReadCacheUseCase =
        ClearBooksThatReadCacheUseCase(repository = repository)

    @Provides
    fun provideClearBooksCacheUseCase(repository: BooksRepository): ClearBooksCacheUseCase =
        ClearBooksCacheUseCase(repository = repository)

    @Provides
    fun provideGetClassUseCase(repository: ClassRepository): GetClassUseCase =
        GetClassUseCase(repository = repository)

}