package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.FragmentAllBooksDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentAllBooksRouterImpl @Inject constructor() : FragmentAllBooksRouter {

    override fun navigateToFragmentDetailsFragment(bookId: String): NavCommand =
        FragmentAllBooksDirections
            .actionFragmentAllBooksToBookInfoNavGraph(bookId = bookId)
            .toNavCommand()

    override fun navigateToEditBookFragment(bookId: String): NavCommand =
        FragmentAllBooksDirections
            .actionFragmentAllBooksToFragmentEditBook(bookId = bookId)
            .toNavCommand()

    override fun navigateToCreateQuestionFragment(bookId: String): NavCommand =
        FragmentAllBooksDirections
            .actionFragmentAllBooksToCreateQuestionNavGraph(bookId = bookId)
            .toNavCommand()

    override fun navigateToBookReadFragment(savedBook: BookThatRead, path: String): NavCommand =
        FragmentAllBooksDirections
            .actionFragmentAllBooksToBookReadNavGraph(book=savedBook,path=path)
            .toNavCommand()
}