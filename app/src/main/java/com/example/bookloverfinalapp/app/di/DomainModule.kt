package com.example.bookloverfinalapp.app.di

import com.example.domain.domain.repository.SchoolRepository
import com.example.domain.usecase.GetAllClassesUseCase
import com.example.domain.usecase.GetAllSchoolsUseCase
import com.example.domain.usecase.GetClassUseCase
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


}
