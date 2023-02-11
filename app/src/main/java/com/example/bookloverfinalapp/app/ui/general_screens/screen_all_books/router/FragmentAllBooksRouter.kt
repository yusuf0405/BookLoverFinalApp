package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentAllBooksRouter {

    fun navigateToFragmentDetailsFragment(bookId: String): NavCommand

    fun navigateToEditBookFragment(bookId: String): NavCommand

    fun navigateToCreateQuestionFragment(bookId: String): NavCommand

    fun navigateToBookReadFragment(savedBook: BookThatRead, path: String): NavCommand

}