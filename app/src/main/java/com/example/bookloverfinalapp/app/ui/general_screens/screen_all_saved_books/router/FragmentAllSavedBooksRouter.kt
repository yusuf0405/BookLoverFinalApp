package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentAllSavedBooksRouter {

    fun navigateToBookChaptersFragment(path: String, savedBook: BookThatRead): NavCommand
}