package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.FragmentAllSavedBooksDirections
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentAllSavedBooksRouterImpl @Inject constructor() : FragmentAllSavedBooksRouter {

    override fun navigateToBookChaptersFragment(path: String, savedBook: BookThatRead) =
        FragmentAllSavedBooksDirections
            .actionFragmentAllSavedBooksToBookReadNavGraph(path = path, book = savedBook)
            .toNavCommand()
}