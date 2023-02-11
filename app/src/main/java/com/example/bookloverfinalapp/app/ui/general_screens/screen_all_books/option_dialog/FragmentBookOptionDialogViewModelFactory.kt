package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.User
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookDomain
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.UserDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.UserCacheRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val BOOK_KEY = "BOOK_KEY"

class FragmentBookOptionDialogViewModelFactory @AssistedInject constructor(
    @Assisted(BOOK_KEY) private val bookId: String,
    private val bookThatReadRepository: BookThatReadRepository,
    private val userCacheRepository: UserCacheRepository,
    private val booksRepository: BooksRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val addBookMapper: Mapper<AddNewBookModel, AddNewBookThatReadDomain>,
    private val userDomainToUiMapper: Mapper<UserDomain, User>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentBookOptionDialogViewModel::class.java)
        return FragmentBookOptionDialogViewModel(
            bookId = bookId,
            savedBooksRepository = bookThatReadRepository,
            userCacheRepository = userCacheRepository,
            dispatchersProvider = dispatchersProvider,
            resourceProvider = resourceProvider,
            savedBookDomainToUiMapper = savedBookDomainToUiMapper,
            addBookMapper = addBookMapper,
            userDomainToUiMapper = userDomainToUiMapper,
            booksRepository = booksRepository,
            bookDomainToBookMapper = bookDomainToBookMapper
        ) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted(BOOK_KEY) bookId: String
        ): FragmentBookOptionDialogViewModelFactory
    }
}