package com.example.bookloverfinalapp.app.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.bookloverfinalapp.app.utils.FetchInternetConnectedStatus
import com.example.bookloverfinalapp.app.utils.FetchInternetConnectedStatusImpl
import com.example.domain.DispatchersProvider
import com.example.domain.use_cases.*
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideFetchBooksFromCloudUseCase(repository: BooksRepository): FetchBooksFromCloudUseCase =
        FetchBooksFromCloudUseCaseImpl(repository = repository)

    @Provides
    fun provideFetchInternetConnectedStatus(
        context: Context
    ): FetchInternetConnectedStatus = FetchInternetConnectedStatusImpl(
        context = context
    )


    @Provides
    fun provideFetchSimilarBooksUseCase(
        repository: BooksRepository,
        audioBooksRepository: AudioBooksRepository,
        dispatchersProvider: DispatchersProvider
    ): FetchSimilarBooksUseCase = FetchSimilarBooksUseCaseImpl(
        repository = repository,
        audioBooksRepository = audioBooksRepository,
        dispatchersProvider = dispatchersProvider
    )

    @Provides
    fun provideBooksThatReadOnRefreshUseCase(repository: BookThatReadRepository): BooksThatReadOnRefreshUseCase =
        BooksThatReadOnRefreshUseCase(repository = repository)


    @Provides
    fun provideStudentsOnRefreshUseCase(repository: UserRepository): StudentsOnRefreshUseCase =
        StudentsOnRefreshUseCase(repository = repository)

    @Provides
    fun provideFetchAllSortedUsersUseCase(
        userCacheRepository: UserCacheRepository,
        userRepository: UserRepository,
    ): FetchAllSortedUsersUseCase =
        FetchAllSortedUsersUseCaseImpl(
            userCacheRepository = userCacheRepository,
            userRepository = userRepository
        )


    @Provides
    fun provideFetchAllSortedBooksUseCase(
        booksRepository: BooksRepository,
    ): FetchAllSortedBooksUseCase =
        FetchAllSortedBooksUseCaseImpl(
            booksRepository = booksRepository,
        )

    @Provides
    fun provideFetchAllMainItemsUseCase(
        userCacheRepository: UserCacheRepository,
        userRepository: UserRepository,
        booksRepository: BooksRepository,
        tasksRepository: TasksRepository,
        dispatchersProvider: DispatchersProvider,
        audioBooksRepository: AudioBooksRepository,
        savedBooksRepository: BookThatReadRepository,
        genresRepository: GenresRepository,
        checkBookIsSavedAndSaveUseCase: CheckBookIsSavedAndSaveUseCase,
    ): FetchAllMainScreenItemsUseCase =
        FetchAllMainScreenItemsUseCaseImpl(
            dispatchersProvider = dispatchersProvider,
            userCacheRepository = userCacheRepository,
            userRepository = userRepository,
            booksRepository = booksRepository,
            audioBooksRepository = audioBooksRepository,
            savedBooksRepository = savedBooksRepository,
            checkBookIsSavedAndSaveUseCase = checkBookIsSavedAndSaveUseCase,
            tasksRepository = tasksRepository,
            genresRepository = genresRepository
        )

    @Provides
    fun provideFetchAllSavedBooksUseCase(
        userCacheRepository: UserCacheRepository,
        savedBooksRepository: BookThatReadRepository,
    ): FetchAllSortedSavedBooksUseCase =
        FetchAllSortedSavedBooksUseCaseImpl(
            userCacheRepository = userCacheRepository,
            savedBooksRepository = savedBooksRepository
        )


    @Provides
    fun provideCheckBookIsSavedAndSaveUseCase(
        booksSaveToFileRepository: BooksSaveToFileRepository,
        booksRepository: BooksRepository,
    ): CheckBookIsSavedAndSaveUseCase = CheckBookIsSavedAndSaveUseCaseImpl(
        booksSaveToFileRepository = booksSaveToFileRepository,
        booksRepository = booksRepository
    )

    @Provides
    fun provideFetchAllSortedAudioBooksUseCase(
        audioBooksRepository: AudioBooksRepository,
    ): FetchAllSortedAudioBooksUseCase = FetchAllSortedAudioBooksUseCaseImpl(
        audioBooksRepository = audioBooksRepository
    )

}