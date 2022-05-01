package com.example.bookloverfinalapp.app.di

import com.example.data.api.KnigolyubApi
import com.example.data.data.cloud.service.BookService
import com.example.data.data.cloud.service.BookThatReadService
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
    fun knigolyubApi(retrofit: Retrofit): KnigolyubApi =
        retrofit.create(KnigolyubApi::class.java)

    @Provides
    @Singleton
    fun bookService(retrofit: Retrofit): BookService =
        retrofit.create(BookService::class.java)

    @Provides
    @Singleton
    fun bookBookThatReadService(retrofit: Retrofit): BookThatReadService =
        retrofit.create(BookThatReadService::class.java)


}