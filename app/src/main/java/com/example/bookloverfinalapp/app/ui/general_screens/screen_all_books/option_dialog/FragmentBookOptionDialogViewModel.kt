package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.App
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsSingleLiveEvent
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.RequestState
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookDomain
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.UserDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.UserCacheRepository
import com.example.domain.use_cases.AddBookToSavedBooksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FragmentBookOptionDialogViewModel @AssistedInject constructor(
   @Assisted private val bookId: String,
    private val savedBooksRepository: BookThatReadRepository,
    private val userCacheRepository: UserCacheRepository,
    private val booksRepository: BooksRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val addBookToSavedBooksUseCase: AddBookToSavedBooksUseCase,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val addBookMapper: Mapper<AddNewBookModel, AddNewBookThatReadDomain>,
    private val userDomainToUiMapper: Mapper<UserDomain, User>,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
) : ViewModel() {

    private var internalSavedBook = BookThatRead.unknown()

    private val bookIdFlow = MutableStateFlow(bookId)

    val bookFlow = bookIdFlow.flatMapLatest(booksRepository::fetchBookObservable)
        .map(bookDomainToBookMapper::map)
        .stateIn(viewModelScope, SharingStarted.Lazily, Book.unknown())

    private val _isErrorNotification = createSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isErrorNotification: SharedFlow<IdResourceString> get() = _isErrorNotification.asSharedFlow()

    private val _isSuccessNotification = createSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isSuccessNotification: SharedFlow<IdResourceString> get() = _isSuccessNotification.asSharedFlow()

    val currentUserFlow = userCacheRepository
        .fetchCurrentUserFromCacheFlow()
        .flowOn(dispatchersProvider.io())
        .map(userDomainToUiMapper::map)
        .stateIn(viewModelScope, SharingStarted.Lazily, User.unknown())

    init {
        viewModelScope.launch(dispatchersProvider.io()) {
            val book = savedBooksRepository.fetchSavedBookByBookIdFromCache(bookId = bookId)
            internalSavedBook = savedBookDomainToUiMapper.map(book)
        }
    }

    fun fetchSavedBook() = internalSavedBook

    fun startDeleteBookInSavedBooks() {
        App.applicationScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { savedBooksRepository.deleteBookInSavedBooks(bookId) },
            onSuccess = {
                emitSuccessNotification(IdResourceString(R.string.book_removed_successfully))
            },
            onError = {
                emitErrorNotification(resourceProvider.fetchIdErrorMessage(it))
            }
        )
    }

    fun startAddBookToSavedBooks() = viewModelScope.launch {
        val isReading = arrayListOf<Boolean>()
        val book = bookFlow.value
        isReading.add(true)
        for (i in 1 until book.chapterCount) isReading.add(false)
        val newBook = AddNewBookModel(
            bookId = book.id,
            page = book.page,
            publicYear = book.publicYear,
            book = book.bookPdf.url,
            title = book.title,
            chapterCount = book.chapterCount,
            poster = BookThatReadPoster(url = book.poster.url, name = book.poster.name),
            isReadingPages = isReading,
            chaptersRead = 0,
            progress = 0,
            author = book.author,
            userId = currentUserFlow.value.id
        )

        addBookToSavedBooksUseCase(
            addBookMapper.map(newBook),
            bookFlow.value.bookPdf.url
        ).onEach { state ->
            when (state) {
                is RequestState.Error -> {
                    emitErrorNotification(resourceProvider.fetchIdErrorMessage(state.error))
                }
                is RequestState.Success -> {
                    emitSuccessNotification(IdResourceString(R.string.book_success_added))
                }
            }
        }.launchIn(App.applicationScope)
    }

    private fun emitErrorNotification(errorMessageId: IdResourceString) {
        _isErrorNotification.tryEmit(errorMessageId)
    }

    private fun emitSuccessNotification(successMessageId: IdResourceString) {
        _isSuccessNotification.tryEmit(successMessageId)
    }

    @AssistedFactory
    interface Factory {
        fun create(
           bookId: String
        ): FragmentBookOptionDialogViewModel
    }
}