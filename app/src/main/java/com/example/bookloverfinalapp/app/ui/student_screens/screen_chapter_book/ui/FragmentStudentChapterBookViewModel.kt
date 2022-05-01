package com.example.bookloverfinalapp.app.ui.student_screens.screen_chapter_book.ui

import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentStudentChapterBookViewModel @Inject constructor(
) : BaseViewModel() {


    fun goReaderFragment(book: BookThatRead, startPage: Int, lastPage: Int, chapter: Int) =
        navigate(FragmentChapterBookDirections
            .actionFragmentChapterBookToFragmentReader(book = book,
                startPage = startPage,
                lastPage = lastPage,
                chapter = chapter))

    fun goBack() = navigateBack()
}