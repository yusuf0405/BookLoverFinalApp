package com.joseph.common.di

import com.joseph.common.CheckInternetConnection
import com.joseph.common.DispatchersProvider
import com.joseph.common.CheckInternetConnectionImpl
import com.joseph.common.DispatchersProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonClassesModule {

    @Binds
    abstract fun bindCheckInternetConnection(
        impl: CheckInternetConnectionImpl
    ): CheckInternetConnection

    @Binds
    abstract fun bindDispatchersProvider(
        impl: DispatchersProviderImpl
    ): DispatchersProvider
}