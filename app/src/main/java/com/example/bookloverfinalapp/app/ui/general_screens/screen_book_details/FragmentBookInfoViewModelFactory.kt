package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.router.FragmentBookInfoRouter
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.use_cases.*
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookDomain
import com.example.domain.models.GenreDomain
import com.example.domain.models.UserDomain
import com.example.domain.repository.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val BOOK_KEY = "BOOK_KEY"

class FragmentBookInfoViewModelFactory @AssistedInject constructor(
    @Assisted(BOOK_KEY) private val bookId: String,
    private val fetchSimilarBooksUseCase: FetchSimilarBooksUseCase,
    private val savedBooksRepository: BookThatReadRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val booksRepository: BooksRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val userRepository: UserRepository,
    private val userCacheRepository: UserCacheRepository,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val router: FragmentBookInfoRouter,
    private val genresRepository: GenresRepository,
    private val userDomainToUIMapper: Mapper<UserDomain, User>,
    private val genreDomainToUiMapper: Mapper<GenreDomain, Genre>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentBookInfoViewModel::class.java)
        return FragmentBookInfoViewModel(
            bookId = bookId,
            fetchSimilarBooksUseCase = fetchSimilarBooksUseCase,
            savedBooksRepository = savedBooksRepository,
            userCacheRepository = userCacheRepository,
            booksSaveToFileRepository = booksSaveToFileRepository,
            booksRepository = booksRepository,
            userRepository = userRepository,
            router = router,
            genresRepository = genresRepository,
            dispatchersProvider = dispatchersProvider,
            resourceProvider = resourceProvider,
            bookDomainToBookMapper = bookDomainToBookMapper,
            userDomainToUIMapper = userDomainToUIMapper,
            genreDomainToUiMapper = genreDomainToUiMapper
        ) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted(BOOK_KEY) bookId: String): FragmentBookInfoViewModelFactory
    }
}