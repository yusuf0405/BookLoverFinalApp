package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.models.UserAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.router.FragmentBookInfoRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.HorizontalItemSecond
import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BookHorizontalItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.HeaderItem
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.RequestState
import com.example.domain.models.*
import com.example.domain.use_cases.FetchSimilarBooksUseCase
import com.example.domain.repository.*
import com.example.domain.use_cases.AddBookToSavedBooksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FragmentBookInfoViewModel @AssistedInject constructor(
    @Assisted private val bookId: String,
    fetchSimilarBooksUseCase: FetchSimilarBooksUseCase,
    private val savedBooksRepository: BookThatReadRepository,
    private val userCacheRepository: UserCacheRepository,
    private val booksRepository: BooksRepository,
    private val genresRepository: GenresRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val addBookToSavedBooksUseCase: AddBookToSavedBooksUseCase,
    private val userRepository: UserRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val router: FragmentBookInfoRouter,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val userDomainToUIMapper: Mapper<UserDomain, User>,
    private val genreDomainToUiMapper: Mapper<GenreDomain, Genre>,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
) : BaseViewModel(), UserItemOnClickListener, BookItemOnClickListener {

    private var _motionPosition = MutableStateFlow(0f)
    val motionPosition get() = _motionPosition.asStateFlow()

    private val bookSavedStatusFlow = MutableStateFlow(SavedStatus.SAVING)
    private val bookIdFlow = MutableStateFlow(bookId)

    var internalSavedBook = MutableStateFlow(BookThatRead.unknown())

    init {
        viewModelScope.launch(dispatchersProvider.io()) {
            val book = savedBooksRepository.fetchSavedBookByBookIdFromCache(bookId = bookId)
            internalSavedBook.tryEmit(savedBookDomainToUiMapper.map(book))
        }
    }

    private val _showSavedBookDeleteDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showSavedBookDeleteDialogFlow get() = _showSavedBookDeleteDialogFlow.asSharedFlow()

    val bookFlow = bookIdFlow.flatMapLatest(booksRepository::fetchBookObservable)
        .map(bookDomainToBookMapper::map)
        .onEach { bookSavedStatusFlow.tryEmit(it.savedStatus) }
        .stateIn(viewModelScope, SharingStarted.Lazily, Book.unknown())

    val genres = bookFlow.map { it.genreIds }.map { genresIds ->
        genresIds.map { genresRepository.fetchGenreFromCache(it) }
    }.map { genres -> genres.map(genreDomainToUiMapper::map) }

    private val _addOrDeleteOperationStatus =
        createMutableSharedFlowAsSingleLiveEvent<SavedStatus>()
    val addOrDeleteOperationStatus: SharedFlow<SavedStatus> =
        _addOrDeleteOperationStatus.asSharedFlow()

    private val _progressDialogIsShowingDialog = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val progressDialogIsShowingDialog get() = _progressDialogIsShowingDialog.asSharedFlow()

    private val _showBookOptionDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showBookOptionDialogFlow get() = _showBookOptionDialogFlow.asSharedFlow()

    private val currentUserFlow = userCacheRepository
        .fetchCurrentUserFromCacheFlow()
        .flowOn(dispatchersProvider.io())
        .stateIn(viewModelScope, SharingStarted.Lazily, UserDomain.unknown())

    val similarBooksFlow = bookFlow.filter { it != Book.unknown() }.flatMapLatest { book ->
        fetchSimilarBooksUseCase(
            genresId = book.genreIds,
            bookId = book.id
        )
    }.flowOn(dispatchersProvider.io())
        .map { it.first.map(bookDomainToBookMapper::map).map(::bookMapToHorizontalItem) }
        .map { listOf(BookHorizontalItem(it)) }
        .map(::addHeaderToSimilarBooks)
        .flowOn(dispatchersProvider.default())
        .catch { exception: Throwable -> Log.i("Joseph", exception.toString()) }

    val reviewersFlow = currentUserFlow.filter { it != UserDomain.unknown() }
        .combine(bookFlow) { user, book -> Pair(user, book) }
        .flatMapLatest(::startFetchAllUsersFromBookId)
        .flowOn(dispatchersProvider.io())
        .map { users -> users.map(userDomainToUIMapper::map).map(::userMapToHorizontalUserMapper) }
        .map { listOf(HorizontalItemSecond(it)) }
        .map(::addHeaderToReviewers)
        .combine(similarBooksFlow) { reviewers, similarBooks ->
            val items = mutableListOf<Item>()
            items.addAll(similarBooks)
            items.addAll(reviewers)
            items
        }
        .flowOn(dispatchersProvider.default())
        .catch { exception: Throwable -> Log.i("Joseph", exception.toString()) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateMotionPosition(position: Float) = _motionPosition.tryEmit(position)

    private fun addHeaderToReviewers(
        reviewers: List<HorizontalItemSecond>,
    ): List<Item> {
        val adapterItems = mutableListOf<Item>()
        if (reviewers.isNotEmpty() && reviewers.first().items.isNotEmpty()) {
            adapterItems.add(
                HeaderItem(
                    titleId = IdResourceString(R.string.reviewers),
                    onClickListener = {},
                    showMoreIsVisible = false
                )
            )
        }
        adapterItems.addAll(reviewers)
        return adapterItems
    }

    private fun addHeaderToSimilarBooks(
        similarBooks: List<BookHorizontalItem>
    ): List<Item> {
        val adapterItems = mutableListOf<Item>()
        if (similarBooks.isNotEmpty() && similarBooks.first().items.isNotEmpty()) {
            adapterItems.add(
                HeaderItem(
                    titleId = IdResourceString(R.string.similar_recommend),
                    onClickListener = {},
                    showMoreIsVisible = false
                )
            )
        }
        adapterItems.addAll(similarBooks)
        return adapterItems
    }

    private fun userMapToHorizontalUserMapper(user: User) = UserAdapterModel(
        id = user.id,
        name = user.name,
        lastName = user.lastname,
        imageUrl = user.image?.url ?: String(),
        listener = this,
    )

    private fun bookMapToHorizontalItem(book: Book) = HorizontalBookAdapterModel(
        id = book.id,
        title = book.title,
        author = book.author,
        description = book.description,
        savedStatus = book.savedStatus,
        posterUrl = book.poster.url,
        listener = this
    )

    fun navigateToEditBookFragment() {
        navigate(router.navigateToEditBookFragment(bookId = bookFlow.value.id))
    }

    fun navigateToGenreInfoFragment(genreId: String) {
        navigate(router.navigateToGenreInfoFragment(genreId = genreId))
    }

    fun navigateToReadBookFragment() {
        val savedBook = internalSavedBook.value
        val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(bookId)
        if (savedBookPath == null) emitToErrorMessageFlow(IdResourceString(R.string.book_is_not_ready))
        else navigate(router.navigateToReadBookFragment(patch = savedBookPath, book = savedBook))
    }

    fun navigateToCreateQuestionFragment() {
        navigate(router.navigateToCreateQuestionFragment(bookId = bookFlow.value.id))
    }

    fun addOrDeleteBookInSavedBooks() {
        when (bookSavedStatusFlow.value) {
            SavedStatus.SAVED -> _showSavedBookDeleteDialogFlow.tryEmit("")
            SavedStatus.NOT_SAVED -> startAddBookToSavedBooks()
            SavedStatus.SAVING -> Unit
        }
    }

    private fun startFetchAllUsersFromBookId(parameters: Pair<UserDomain, Book>) =
        userRepository.fetchAllUsersFromBookId(
            bookId = parameters.second.id,
            currentUserId = parameters.first.id,
            schoolId = parameters.first.schoolId
        )

    fun startDeleteBookInSavedBooks() = viewModelScope.launchSafe(
        dispatcher = dispatchersProvider.io(),
        safeAction = { savedBooksRepository.deleteBookInSavedBooks(bookFlow.value.id) },
        onStart = { emitProgressDialogIsShowingDialog(isShow = true) },
        onSuccess = {
            emitAddOrDeleteOperationFlow((SavedStatus.NOT_SAVED))
            dismissProgressDialog()
        },
        onError = {
            emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it))
            dismissProgressDialog()
        }
    )


    private fun startAddBookToSavedBooks() = viewModelScope.launch {
        addBookToSavedBooksUseCase(
            createAddNewBookModel(),
            bookFlow.value.bookPdf.url
        ).onEach { state ->
            when (state) {
                is RequestState.Error -> {
                    emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(state.error))
                    dismissProgressDialog()
                }
                is RequestState.Success -> {
                    emitAddOrDeleteOperationFlow((SavedStatus.SAVED))
                    dismissProgressDialog()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun createAddNewBookModel(): AddNewBookThatReadDomain {
        val book = bookFlow.value
        val isReading = arrayListOf<Boolean>()
        isReading.add(true)
        for (i in 1 until book.chapterCount) isReading.add(false)
        return AddNewBookThatReadDomain(
            bookId = book.id,
            page = book.page,
            publicYear = book.publicYear,
            book = book.bookPdf.url,
            title = book.title,
            chapterCount = book.chapterCount,
            poster = BookThatReadPosterDomain(url = book.poster.url, name = book.poster.name),
            isReadingPages = isReading,
            chaptersRead = 0,
            progress = 0,
            author = book.author,
            userId = currentUserFlow.value.id
        )
    }

    private fun dismissProgressDialog() {
        emitProgressDialogIsShowingDialog(isShow = false)
    }

    private fun emitProgressDialogIsShowingDialog(isShow: Boolean) {
        _progressDialogIsShowingDialog.tryEmit(isShow)
    }

    private fun emitAddOrDeleteOperationFlow(savedStatus: SavedStatus) =
        _addOrDeleteOperationStatus.tryEmit(savedStatus)

    override fun userItemOnClickListener(userId: String) {

    }

    override fun bookItemOnClick(bookId: String) {
        bookIdFlow.tryEmit(bookId)

    }

    override fun bookOptionMenuOnClick(bookId: String) {
        _showBookOptionDialogFlow.tryEmit(bookId)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            bookId: String
        ): FragmentBookInfoViewModel
    }
}