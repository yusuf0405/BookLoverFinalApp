package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentReaderRouter {

    fun navigateToQuestionnaireFragment(
        savedBook: BookThatRead,
        path: String,
        chapter: Int
    ): NavCommand

    fun navigateToBookInfoFragment(bookId:String): NavCommand

}