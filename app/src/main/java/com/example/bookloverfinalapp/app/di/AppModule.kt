package com.example.bookloverfinalapp.app.di

import android.content.Context
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.communication.ErrorCommunication
import com.example.bookloverfinalapp.app.utils.communication.NavigationCommunication
import com.example.bookloverfinalapp.app.utils.communication.ProgressCommunication
import com.example.bookloverfinalapp.app.utils.communication.ProgressDialogCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.Dispatchers
import com.example.data.ResourceProvider
import com.example.data.base.ResponseHandler
import com.example.data.base.ResponseHandlerImpl
import com.example.domain.DispatchersProvider
import com.example.domain.use_cases.*
import com.example.domain.repository.*
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
    fun provideActivity(): ActivityMain = ActivityMain()

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
    @Singleton
    fun provideResponseHandler(
        dispatchersProvider: DispatchersProvider
    ): ResponseHandler = ResponseHandlerImpl(
        dispatchersProvider = dispatchersProvider
    )


    @Provides
    fun provideClearStudentsCacheUseCase(
        userRepository: UserRepository,
        booksRepository: BooksRepository,
        audioBooksRepository: AudioBooksRepository,
        bookThatReadRepository: BookThatReadRepository,
        classRepository: ClassRepository
    ): ClearAllAppCacheUseCase =
        ClearAllAppCacheUseCaseImpl(
            userRepository = userRepository,
            booksRepository = booksRepository,
            audioBooksRepository = audioBooksRepository,
            bookThatReadRepository = bookThatReadRepository,
            classRepository = classRepository
        )
}