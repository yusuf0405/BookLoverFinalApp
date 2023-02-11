package com.example.bookloverfinalapp.app.ui.general_screens.screen_search.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.FragmentSearchDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentSearchRouterImpl @Inject constructor() : FragmentSearchRouter {

    override fun navigateToBookInfoFragment(bookId: String): NavCommand =
        FragmentSearchDirections.actionFragmentSearchToBookInfoNavGraph(bookId)
            .toNavCommand()

    override fun navigateToUserInfoFragment(userId: String): NavCommand = FragmentSearchDirections
        .actionFragmentSearchToUserInfoNavGraph(userId = userId)
        .toNavCommand()

    override fun navigateToCreateQuestionFragment(bookId: String): NavCommand =
        FragmentSearchDirections
            .actionFragmentSearchToCreateQuestionNavGraph(bookId = bookId)
            .toNavCommand()

    override fun navigateToEditBookFragment(bookId: String): NavCommand = FragmentSearchDirections
        .actionFragmentSearchToFragmentEditBook(bookId = bookId)
        .toNavCommand()

    override fun navigateToBookReadBlockFragment(
        patch: String,
        savedBook: BookThatRead
    ): NavCommand = FragmentSearchDirections
        .actionFragmentSearchToBookReadNavGraph(book = savedBook, patch)
        .toNavCommand()
}