package com.example.bookloverfinalapp.app.di

import com.example.domain.domain.interactor.*
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorModule {

    @Provides
    fun provideGetAllBooksUseCase(repository: BooksRepository): GetAllBooksUseCase =
        GetAllBooksUseCase(repository = repository)


    @Provides
    fun provideClearBooksCacheUseCase(repository: BooksRepository): ClearBooksCacheUseCase =
        ClearBooksCacheUseCase(repository = repository)

    @Provides
    fun provideGetBookForReadingUseCase(repository: BooksRepository): GetBookForReadingUseCase =
        GetBookForReadingUseCase(repository = repository)

    @Provides
    fun provideGetAllChapterQuestionsUseCase(repository: BooksRepository): GetAllChapterQuestionsUseCase =
        GetAllChapterQuestionsUseCase(repository = repository)

    @Provides
    fun provideGetBookThatReadUseCase(repository: BookThatReadRepository): GetBookThatReadUseCase =
        GetBookThatReadUseCase(repository = repository)

    @Provides
    fun provideGetMyStudentBooksUseCase(repository: BookThatReadRepository): GetMyStudentBooksUseCase =
        GetMyStudentBooksUseCase(repository = repository)


    @Provides
    fun provideDeleteFromMyBooksUseCase(repository: BookThatReadRepository): DeleteFromMyBooksUseCase =
        DeleteFromMyBooksUseCase(repository = repository)

    @Provides
    fun provideClearBooksThatReadCacheUseCase(repository: BookThatReadRepository): ClearBooksThatReadCacheUseCase =
        ClearBooksThatReadCacheUseCase(repository = repository)


    @Provides
    fun provideGetMyBookUseCase(repository: BookThatReadRepository): GetMyBookUseCase =
        GetMyBookUseCase(repository = repository)


    @Provides
    fun provideAddNewStudentBookUseCase(repository: BookThatReadRepository): AddNewBookThatReadUseCase =
        AddNewBookThatReadUseCase(repository = repository)

    @Provides
    fun provideUpdateChaptersUseCase(repository: BookThatReadRepository): UpdateChaptersUseCase =
        UpdateChaptersUseCase(repository = repository)


    @Provides
    fun provideUpdateProgressUseCase(repository: BookThatReadRepository): UpdateProgressUseCase =
        UpdateProgressUseCase(repository = repository)

    @Provides
    fun provideUpdateStudentUseCase(repository: UserRepository): GetMyStudentsUseCase =
        GetMyStudentsUseCase(repository = repository)


}