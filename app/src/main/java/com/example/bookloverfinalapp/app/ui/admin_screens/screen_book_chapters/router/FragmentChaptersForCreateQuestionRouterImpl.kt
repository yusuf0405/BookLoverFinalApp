package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.router

import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.ui.FragmentChaptersForCreateQuestionDirections
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentChaptersForCreateQuestionRouterImpl @Inject constructor() :
    FragmentChaptersForCreateQuestionRouter {

    override fun navigateNavigateToCreateQuestionFragment(bookId: String, chapter: Int) =
        FragmentChaptersForCreateQuestionDirections.actionFragmentAdminChaptersToFragmentCreateNewQuestion(
            bookId = bookId,
            chapter = chapter
        ).toNavCommand()
}