package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.base.sideeffects.dialogs.DialogsManager
import com.example.bookloverfinalapp.app.base.sideeffects.dialogs.DialogsManagerImpl
import com.example.bookloverfinalapp.app.base.sideeffects.snackbar.SnackbarManager
import com.example.bookloverfinalapp.app.base.sideeffects.snackbar.SnackbarManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SideEffectsModule {

    @Binds
    abstract fun bindSnackbarManager(impl: SnackbarManagerImpl): SnackbarManager

    @Binds
    abstract fun bindDialogsManager(impl: DialogsManagerImpl): DialogsManager


}