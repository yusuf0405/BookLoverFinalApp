package com.example.bookloverfinalapp.app.di

import com.example.domain.domain.interactor.DeleteFromMyBooksUseCase
import com.example.domain.domain.interactor.GetAllBooksUseCase
import com.example.domain.domain.interactor.GetStudentBookUseCase
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
    fun provideGetStudentBookUseCase(repository: StudentBooksRepository): GetStudentBookUseCase =
        GetStudentBookUseCase(repository = repository)


    @Provides
    fun provideDeleteFromMyBooksUseCase(repository: StudentBooksRepository): DeleteFromMyBooksUseCase =
        DeleteFromMyBooksUseCase(repository = repository)


}