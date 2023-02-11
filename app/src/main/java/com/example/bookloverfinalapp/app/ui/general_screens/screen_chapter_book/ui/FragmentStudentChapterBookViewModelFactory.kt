package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.router.FragmentChapterBookRouter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val BOOK_KEY = "BOOK_KEY"
private const val BOOK_PATCH_KEY = "BOOK_PATCH_KEY"
class FragmentStudentChapterBookViewModelFactory @AssistedInject constructor(
    @Assisted(BOOK_KEY) private val book: BookThatRead,
    @Assisted(BOOK_PATCH_KEY) private val patch: String,
    private val router: FragmentChapterBookRouter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentStudentChapterBookViewModel::class.java)
        return FragmentStudentChapterBookViewModel(
            book = book,
            patch = patch,
            router = router
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(BOOK_KEY) book: BookThatRead,
            @Assisted(BOOK_PATCH_KEY) patch: String,
        ): FragmentStudentChapterBookViewModelFactory
    }
}