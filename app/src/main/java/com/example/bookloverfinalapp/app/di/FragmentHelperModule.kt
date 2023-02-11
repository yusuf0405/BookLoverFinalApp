package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.BookPdfHelper
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.BookPdfHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class FragmentHelperModule {

    @Binds
    abstract fun provideBookPdfHelper(impl: BookPdfHelperImpl): BookPdfHelper
}
