package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.router

import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.models.CreateQuestionType
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentChaptersForCreateQuestionRouter {

    fun navigateNavigateToCreateQuestionFragment(
        bookId: String,
        chapter: Int,
        type: CreateQuestionType
    ): NavCommand
}