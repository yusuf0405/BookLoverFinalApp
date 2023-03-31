package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_reader.FragmentReaderDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentReaderRouterImpl @Inject constructor() : FragmentReaderRouter {

    override fun navigateToQuestionnaireFragment(
        savedBook: BookThatRead,
        path: String,
        chapter: Int
    ): NavCommand = FragmentReaderDirections.actionFragmentStudentReaderToQuestionnaireNavGraph(
        book = savedBook,
        chapter = chapter,
        path = path
    ).toNavCommand()


    override fun navigateToBookInfoFragment(bookId: String): NavCommand =
        FragmentReaderDirections
            .actionFragmentStudentReaderToBookInfoNavGraph(bookId)
            .toNavCommand()

}