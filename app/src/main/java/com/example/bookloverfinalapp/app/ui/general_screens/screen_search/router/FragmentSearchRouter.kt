package com.example.bookloverfinalapp.app.ui.general_screens.screen_search.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentSearchRouter {

    fun navigateToBookInfoFragment(bookId: String): NavCommand

    fun navigateToUserInfoFragment(userId: String): NavCommand

    fun navigateToCreateQuestionFragment(bookId: String): NavCommand

    fun navigateToEditBookFragment(bookId: String): NavCommand

    fun navigateToBookReadBlockFragment(patch: String, savedBook: BookThatRead): NavCommand
}