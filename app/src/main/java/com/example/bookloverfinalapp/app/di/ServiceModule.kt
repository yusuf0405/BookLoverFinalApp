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


}