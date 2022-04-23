package com.example.bookloverfinalapp.app.di

import com.example.domain.repository.BookRepository
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.SchoolRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetAllClassesUseCase(repository: SchoolRepository): GetAllClassesUseCase =
        GetAllClassesUseCase(repository = repository)

    @Provides
    fun provideGetAllSchoolsUseCase(repository: SchoolRepository): GetAllSchoolsUseCase =
        GetAllSchoolsUseCase(repository = repository)

    @Provides
    fun provideGetClassUseCase(repository: SchoolRepository): GetClassUseCase =
        GetClassUseCase(repository = repository)

    @Provides
    fun provideSignUpUseCase(repository: LoginRepository): SignUpUseCase =
        SignUpUseCase(repository = repository)

    @Provides
    fun provideSignInUseCase(repository: LoginRepository): SignInUseCase =
        SignInUseCase(repository = repository)


    @Provides
    fun provideGetBookForReadingUseCase(repository: BookRepository): GetBookForReadingUseCase =
        GetBookForReadingUseCase(repository = repository)

    @Provides
    fun provideGetStudentProgressUseCase(repository: BookRepository): GetStudentProgressUseCase =
        GetStudentProgressUseCase(repository = repository)


    @Provides
    fun provideUpdateStudentUseCase(repository: UserRepository): UpdateUserUseCase =
        UpdateUserUseCase(repository = repository)

    @Provides
    fun provideAddNewStudentBookUseCase(repository: BookRepository): AddNewStudentBookUseCase =
        AddNewStudentBookUseCase(repository = repository)


    @Provides
    fun provideUpdateProgressStudentBookUseCase(repository: BookRepository): UpdateProgressStudentBookUseCase =
        UpdateProgressStudentBookUseCase(repository = repository)

    @Provides
    fun provideGetMyBookUseCase(repository: BookRepository): GetMyBookUseCase =
        GetMyBookUseCase(repository = repository)

    @Provides
    fun provideGetAllChapterQuestionsUseCase(repository: BookRepository): GetAllChapterQuestionsUseCase =
        GetAllChapterQuestionsUseCase(repository = repository)


}
