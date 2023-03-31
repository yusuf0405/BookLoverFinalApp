package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.ui.service_player.PlaybackManager
import com.example.bookloverfinalapp.app.ui.service_player.PlaybackManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HelperModule {

    @Binds
    abstract fun providePlaybackManager(impl: PlaybackManagerImpl): PlaybackManager


}