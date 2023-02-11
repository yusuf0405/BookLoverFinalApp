package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.ui.FragmentChapterBookDirections
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentChapterBookRouterImpl @Inject constructor() : FragmentChapterBookRouter {

    override fun navigateToReaderFragment(
        book: BookThatRead,
        startPage: Int,
        lastPage: Int,
        chapter: Int,
        path: String,
        chapterTitle: String
    ) = FragmentChapterBookDirections.actionFragmentChapterBookToFragmentStudentReader(
        book = book,
        startPage = startPage,
        lastPage = lastPage,
        chapter = chapter,
        path = path,
        chapterTitle = chapterTitle
    ).toNavCommand()
}