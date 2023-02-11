package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentBookInfoRouter {

    fun navigateToEditBookFragment(bookId: String): NavCommand

    fun navigateToReadBookFragment(book: BookThatRead, patch: String): NavCommand

    fun navigateToGenreInfoFragment(genreId: String): NavCommand

    fun navigateToCreateQuestionFragment(bookId: String): NavCommand
}