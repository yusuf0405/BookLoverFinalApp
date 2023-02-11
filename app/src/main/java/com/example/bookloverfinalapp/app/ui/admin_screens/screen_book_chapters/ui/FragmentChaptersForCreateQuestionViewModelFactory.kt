package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.router.FragmentChaptersForCreateQuestionRouter
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.BookDomain
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.BooksSaveToFileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val BOOK_ID_KEY = "BOOK_ID_KEY"

class FragmentChaptersForCreateQuestionViewModelFactory @AssistedInject constructor(
    @Assisted(BOOK_ID_KEY) private val bookId: String,
    private val booksRepository: BooksRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val router: FragmentChaptersForCreateQuestionRouter,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentChaptersForCreateQuestionViewModel::class.java)
        return FragmentChaptersForCreateQuestionViewModel(
            bookId = bookId,
            booksRepository = booksRepository,
            booksSaveToFileRepository = booksSaveToFileRepository,
            dispatchersProvider = dispatchersProvider,
            router = router,
            bookDomainToBookMapper = bookDomainToBookMapper
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(BOOK_ID_KEY) bookId: String
        ): FragmentChaptersForCreateQuestionViewModelFactory
    }
}