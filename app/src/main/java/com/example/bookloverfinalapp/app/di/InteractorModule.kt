package com.example.bookloverfinalapp.app.di

import com.example.domain.domain.interactor.*
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.domain.repository.StudentBooksRepository
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
    fun provideGetBookForReadingUseCase(repository: BooksRepository): GetBookForReadingUseCase =
        GetBookForReadingUseCase(repository = repository)


    @Provides
    fun provideGetStudentBookUseCase(repository: StudentBooksRepository): GetStudentBookUseCase =
        GetStudentBookUseCase(repository = repository)


    @Provides
    fun provideDeleteFromMyBooksUseCase(repository: StudentBooksRepository): DeleteFromMyBooksUseCase =
        DeleteFromMyBooksUseCase(repository = repository)


    @Provides
    fun provideGetMyBookUseCase(repository: StudentBooksRepository): GetMyBookUseCase =
        GetMyBookUseCase(repository = repository)


    @Provides
    fun provideAddNewStudentBookUseCase(repository: StudentBooksRepository): AddNewStudentBookUseCase =
        AddNewStudentBookUseCase(repository = repository)

    @Provides
    fun provideUpdateChaptersUseCase(repository: StudentBooksRepository): UpdateChaptersUseCase =
        UpdateChaptersUseCase(repository = repository)


    @Provides
    fun provideUpdateProgressUseCase(repository: StudentBooksRepository): UpdateProgressUseCase =
        UpdateProgressUseCase(repository = repository)


}