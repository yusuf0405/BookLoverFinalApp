package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentChapterBookRouter {

    fun navigateToReaderFragment(
        book: BookThatRead,
        startPage: Int,
        lastPage: Int,
        chapter: Int,
        path: String,
        chapterTitle: String
    ): NavCommand
}