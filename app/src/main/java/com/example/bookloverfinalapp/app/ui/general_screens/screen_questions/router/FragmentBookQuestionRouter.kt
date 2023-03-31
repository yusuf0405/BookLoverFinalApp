package com.example.bookloverfinalapp.app.ui.general_screens.screen_questions.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentBookQuestionRouter {

    fun navigateToBookReadFragment(savedBook: BookThatRead, path: String): NavCommand
}