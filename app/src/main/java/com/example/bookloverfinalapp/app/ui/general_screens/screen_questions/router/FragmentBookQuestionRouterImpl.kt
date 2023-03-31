package com.example.bookloverfinalapp.app.ui.general_screens.screen_questions.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_questions.FragmentBookQuestionDirections
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentBookQuestionRouterImpl @Inject constructor() : FragmentBookQuestionRouter {

    override fun navigateToBookReadFragment(savedBook: BookThatRead, path: String) =
        FragmentBookQuestionDirections.actionFragmentBookQuestionToBookReadNavGraph(
            book = savedBook,
            path = path
        ).toNavCommand()

}