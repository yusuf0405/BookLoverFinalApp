package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.FragmentGenreInfoDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentGenreInfoRouterImpl @Inject constructor() : FragmentGenreInfoRouter {

    override fun navigateToBookInfoFragment(bookId: String) = FragmentGenreInfoDirections
        .actionFragmentGenreInfoToBookInfoNavGraph(bookId = bookId)
        .toNavCommand()

    override fun navigateToEditBookFragment(bookId: String): NavCommand =
        FragmentGenreInfoDirections
            .actionFragmentGenreInfoToFragmentEditBook(bookId = bookId)
            .toNavCommand()


    override fun navigateToCreateQuestionFragment(bookId: String): NavCommand =
        FragmentGenreInfoDirections
            .actionFragmentGenreInfoToCreateQuestionNavGraph(bookId = bookId)
            .toNavCommand()

    override fun navigateToBookReadFragment(savedBook: BookThatRead, path: String): NavCommand =
        FragmentGenreInfoDirections
            .actionFragmentGenreInfoToBookReadNavGraph(book = savedBook, path = path)
            .toNavCommand()

}