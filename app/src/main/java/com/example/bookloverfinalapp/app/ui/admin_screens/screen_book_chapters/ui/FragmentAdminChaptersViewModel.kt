package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.ui

import com.example.bookloverfinalapp.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentAdminChaptersViewModel @Inject constructor() : BaseViewModel() {

    fun goBack() = navigateBack()


}