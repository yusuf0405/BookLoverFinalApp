package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.FragmentBookInfoDirections
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.FragmentGenreInfoDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentBookInfoRouterImpl @Inject constructor() : FragmentBookInfoRouter {

    override fun navigateToEditBookFragment(bookId: String) = FragmentBookInfoDirections
        .actionFragmentBookInfoToFragmentEditBook(bookId = bookId)
        .toNavCommand()

    override fun navigateToReadBookFragment(book: BookThatRead, patch: String) =
        FragmentBookInfoDirections
            .actionFragmentBookInfoToBookReadNavGraph(book = book, path = patch)
            .toNavCommand()

    override fun navigateToGenreInfoFragment(genreId: String): NavCommand =
        FragmentBookInfoDirections
            .actionFragmentBookInfoToBookGenreInfoNavGraph(genreId = genreId)
            .toNavCommand()

    override fun navigateToCreateQuestionFragment(bookId: String) = FragmentBookInfoDirections
        .actionFragmentBookInfoToCreateQuestionNavGraph(bookId = bookId)
        .toNavCommand()
}