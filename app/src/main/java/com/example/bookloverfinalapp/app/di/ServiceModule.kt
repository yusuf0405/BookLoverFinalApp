package com.example.bookloverfinalapp.app.di

import com.example.data.cloud.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideSchoolService(retrofit: Retrofit): SchoolService =
        retrofit.create(SchoolService::class.java)

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit): BookService =
        retrofit.create(BookService::class.java)

    @Provides
    @Singleton
    fun provideBookThatReadService(retrofit: Retrofit): BookThatReadService =
        retrofit.create(BookThatReadService::class.java)


    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideClassService(retrofit: Retrofit): ClassService =
        retrofit.create(ClassService::class.java)

    @Provides
    @Singleton
    fun provideAudioBookService(retrofit: Retrofit): AudioBookService =
        retrofit.create(AudioBookService::class.java)

    @Provides
    @Singleton
    fun provideTaskService(retrofit: Retrofit): TaskService =
        retrofit.create(TaskService::class.java)

    @Provides
    @Singleton
    fun provideGenreService(retrofit: Retrofit): GenreService =
        retrofit.create(GenreService::class.java)

    @Provides
    @Singleton
    fun provideStoriesService(retrofit: Retrofit): StoriesService =
        retrofit.create(StoriesService::class.java)

    @Provides
    @Singleton
    fun provideQuestionsService(retrofit: Retrofit): QuestionsService =
        retrofit.create(QuestionsService::class.java)

}