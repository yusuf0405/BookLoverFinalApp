package com.example.bookloverfinalapp.app.glue.profile.di

import com.example.bookloverfinalapp.app.glue.profile.mappers.UserDomainToUserFeatureModelMapper
import com.example.domain.models.UserDomain
import com.joseph.common.Mapper
import com.joseph.profile.domain.models.UserFeatureModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {

    @Binds
    abstract fun bindUserDomainToUserFeatureModelMapper(
        impl: UserDomainToUserFeatureModelMapper
    ): Mapper<UserDomain, UserFeatureModel>

}