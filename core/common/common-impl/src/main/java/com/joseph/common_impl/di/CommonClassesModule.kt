package com.joseph.common_impl.di

import com.joseph.common_api.CheckInternetConnection
import com.joseph.common_api.DispatchersProvider
import com.joseph.common_impl.CheckInternetConnectionImpl
import com.joseph.common_impl.DispatchersProviderImpl
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