package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentGenreInfoRouter {

    fun navigateToBookInfoFragment(bookId: String): NavCommand

    fun navigateToEditBookFragment(bookId: String): NavCommand

    fun navigateToCreateQuestionFragment(bookId: String): NavCommand

    fun navigateToBookReadFragment(savedBook: BookThatRead, path: String): NavCommand

}