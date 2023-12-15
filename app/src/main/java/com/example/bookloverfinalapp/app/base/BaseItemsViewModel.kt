@file:OptIn(FlowPreview::class)

package com.example.bookloverfinalapp.app.base

import android.os.Parcelable
import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog.SortingAudioBookSelectOptionActionHandler
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.router.FragmentAllBooksRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.SortingBookSelectOptionActionHandler
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres.router.FragmentAllGenresRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.router.FragmentAllSavedBooksRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog.SortingSavedBookSelectOptionActionHandler
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers.UserDomainToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.models.UserAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog.SortingUsersSelectOptionActionHandler
import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.AudioBookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookGenreItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.GenreItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.*
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.*
import com.example.domain.repository.*
import com.example.domain.sort_dialog.BookSortOrder
import com.example.domain.use_cases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BaseItemsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userCacheRepository: UserCacheRepository,
    private val savedBooksRepository: BookThatReadRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val fetchAllSortedSavedBooksUseCase: FetchAllSortedSavedBooksUseCase,
    private val tasksRepository: TasksRepository,
    private val genresRepository: GenresRepository,
    private val fetchAllSortedBooksUseCase: FetchAllSortedBooksUseCase,
    private val fetchAllSortedUsersUseCase: FetchAllSortedUsersUseCase,
    private val fetchAllSortedAudioBooksUseCase: FetchAllSortedAudioBooksUseCase,
    private val sortingUsersSelectOptionActionHandler: SortingUsersSelectOptionActionHandler,
    private val sortingAudioBookSelectOptionActionHandler: SortingAudioBookSelectOptionActionHandler,
    private val dispatchersProvider: DispatchersProvider,
    private val fragmentAllGenresRouter: FragmentAllGenresRouter,
    private val userDomainToUserUiMapper: Mapper<StudentDomain, Student>,
    private val userDomainToAdapterModelMapper: UserDomainToAdapterModelMapper,
    private val sortingBookSelectOptionActionHandler: SortingBookSelectOptionActionHandler,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val sortingSavedBookSelectOptionActionHandler: SortingSavedBookSelectOptionActionHandler,
    private val savedBookDomainToBookMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>,
    private val taskDomainToAdapterModelMapper: Mapper<TaskDomain, TaskAdapterModel>,
    private val router: FragmentAllSavedBooksRouter,
    private val fragmentAllBooksRouter: FragmentAllBooksRouter,
) : ViewModel(), BookItemOnClickListener, SearchView.OnQueryTextListener,
    SavedBookItemOnClickListeners, UserItemOnClickListener, GenreItemOnClickListeners,
    AudioBookItemOnClickListener, BookGenreItemOnClickListeners {

    private val searchStringFlow = MutableStateFlow(String())
    private val _recyclerViewStateFlow = MutableStateFlow<Parcelable?>(null)
    private val allItemsFetchTypeFlow = MutableStateFlow(AllItemsFetchType.NONE)

    private var _motionPosition = MutableStateFlow(0f)
    val motionPosition get() = _motionPosition.asStateFlow()

    private var _navCommand = createMutableSharedFlowAsSingleLiveEvent<NavCommand>()
    val navCommand: SharedFlow<NavCommand> get() = _navCommand.asSharedFlow()

    private val _isErrorMessageIdFlow = createMutableSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isErrorMessageIdFlow: SharedFlow<IdResourceString> get() = _isErrorMessageIdFlow.asSharedFlow()

    private val _isErrorMessageFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val isErrorMessageFlow: SharedFlow<String> get() = _isErrorMessageFlow.asSharedFlow()

    private val _showSuccessSnackbarFlow =
        createMutableSharedFlowAsSingleLiveEvent<IdResourceString>()
    val showSuccessSnackbarFlow get() = _showSuccessSnackbarFlow.asSharedFlow()

    private val _showConfirmDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showConfirmDialogFlow get() = _showConfirmDialogFlow.asSharedFlow()

    private val _playAudioBookFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val playAudioBookFlow get() = _playAudioBookFlow.asSharedFlow()

    private val _showBookOptionDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showBookOptionDialogFlow get() = _showBookOptionDialogFlow.asSharedFlow()

    val allItemsFlow = allItemsFetchTypeFlow.flatMapLatest { allItemsType ->
        val flow = when (allItemsType) {
            AllItemsFetchType.ALL_BOOKS -> allFilteredBooksFlow
            AllItemsFetchType.ALL_SAVED_BOOKS -> userSavedBooksFlow
            AllItemsFetchType.ALL_USERS -> allFilteredUsersFlow
            AllItemsFetchType.ALL_GENRES -> allGenresFlow
            AllItemsFetchType.ALL_TASKS -> allTasksFlow
            AllItemsFetchType.ALL_AUDIO_BOOKS -> allAudioBooksFlow
            AllItemsFetchType.NONE -> flow { }
        }
        flow
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val currentUserFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    private val sortingBookSelectOptionValueFlow =
        sortingBookSelectOptionActionHandler.observeSelections()
            .map { currentAllBooksSortOrder() }
            .stateIn(viewModelScope, SharingStarted.Lazily, currentAllBooksSortOrder())

    private val sortingAudioBookSelectOptionValueFlow =
        sortingAudioBookSelectOptionActionHandler.observeSelections()
            .map { currentAllAudioBooksSortOrder() }
            .stateIn(viewModelScope, SharingStarted.Lazily, currentAllAudioBooksSortOrder())

    private val userParametersAndAudioSortOrder = currentUserFlow.map { it.schoolId }
        .combine(sortingAudioBookSelectOptionValueFlow)
        { parameters, sort -> Pair(parameters, sort) }


    private val allTasksFlow = currentUserFlow.map { it.classId }
        .flatMapLatest(tasksRepository::fetchAllClassTasks)
        .map { tasks -> tasks.map(taskDomainToAdapterModelMapper::map) }
        .map {
            val items = mutableListOf<Item>()
            items.addAll(it)
            items.addAll(it)
            items.addAll(it)
            items.addAll(it)
            items
        }

    private val allAudioBooksFlow = userParametersAndAudioSortOrder.flatMapLatest {
        fetchAllSortedAudioBooksUseCase(schoolId = it.first, order = it.second)
    }.map { audioBooks -> audioBooks.map(audioBookDomainToUiMapper::map) }
        .map { audioBooks -> audioBooks.map { AudioBookAdapterModel(it, listener = this) } }
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchString -> applyAudioBookSearchFiltered(items, searchString) }
        .map(::addSearchViewAdapterModel)
        .flowOn(dispatchersProvider.default())
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val userParametersAndBookSortOrder = currentUserFlow.map { Pair(it.id, it.schoolId) }
        .combine(sortingBookSelectOptionValueFlow)
        { parameters, sort -> Pair(parameters, sort) }

    private val allBooksAdapterModelsFlow = userParametersAndBookSortOrder
        .flatMapLatest(::startFetchAllBooks)
        .flowOn(dispatchersProvider.io())
        .map(::mapBooksDomainToBook)
        .map(::mapBooksToAdapterItems)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val allFilteredBooksFlow = allBooksAdapterModelsFlow
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchString -> applyBookSearchFiltered(items, searchString) }
        .map(::addSearchViewAdapterModel)
        .flowOn(dispatchersProvider.default())
        .catch { exception: Throwable ->
            emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(exception))
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val currentSortValueFlow = sortingSavedBookSelectOptionActionHandler.observeSelections()
        .map { currentAllSavedOrder() }
        .stateIn(viewModelScope, SharingStarted.Lazily, currentAllSavedOrder())

    private val userSavedBooksFlow = currentSortValueFlow
        .flatMapLatest(fetchAllSortedSavedBooksUseCase::invoke)
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchString -> applySavedBookSearchFiltered(items, searchString) }
        .map(::mapSavedBooksToAdapterModel)
        .map(::addSearchViewAdapterModel)
        .flowOn(dispatchersProvider.default())
        .catch { error: Throwable ->
            emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(error))
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val sortingUsersSelectOptionValueFlow =
        sortingUsersSelectOptionActionHandler.observeSelections()
            .map { currentAllUsersOrder() }
            .stateIn(viewModelScope, SharingStarted.Lazily, currentAllUsersOrder())

    private val allFilteredUsersFlow = sortingUsersSelectOptionValueFlow
        .flatMapLatest(fetchAllSortedUsersUseCase::invoke)
        .map { users -> users.map(userDomainToUserUiMapper::map) }
        .map { users -> users.map { userDomainToAdapterModelMapper.map(it, listener = this) } }
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchString -> applyAllUsersSearchFiltered(items, searchString) }
        .map(::addSearchViewAdapterModel)
        .flowOn(dispatchersProvider.default())
        .catch { exception: Throwable -> emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(exception))
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val allGenresFlow = genresRepository.fetchAllGenres()
        .flowOn(dispatchersProvider.io())
        .map { genres ->
            genres.map { genre ->
                BookGenreItem(
                    id = genre.id,
                    titles = genre.titles,
                    posterUrl = genre.poster.url,
                    listener = this
                )
            }
        }.flowOn(dispatchersProvider.io())
        .combine(searchStringFlow.debounce(BaseViewModel.SEARCH_DEBOUNCE))
        { items, searchString -> applyAllGenresSearchFiltered(items, searchString) }
        .map(::addSearchViewAdapterModel)
        .flowOn(dispatchersProvider.default())
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveRecyclerViewCurrentState(state: Parcelable?) = _recyclerViewStateFlow.tryEmit(state)

    fun fetchRecyclerViewCurrentState(): Parcelable? = _recyclerViewStateFlow.value

    fun updateMotionPosition(position: Float) = _motionPosition.tryEmit(position)

    fun updateAllItemsTypeFlow(allItemsFetchType: AllItemsFetchType) =
        allItemsFetchTypeFlow.tryEmit(allItemsFetchType)

    fun navigateToEditBookFragment(bookId: String) {
        fragmentAllBooksRouter.navigateToEditBookFragment(bookId)
    }

    fun navigateToCreateQuestionFragment(bookId: String) {
        fragmentAllBooksRouter.navigateToCreateQuestionFragment(bookId)
    }

    fun navigateToBookReadFragment(savedBook: BookThatRead) {
        fragmentAllBooksRouter.navigateToBookReadFragment(
            savedBook = savedBook,
            path = checkAndGetSavedBookRath(savedBook),
        )
    }

    private fun currentAllBooksSortOrder() = sortingBookSelectOptionActionHandler.toOrder()

    private fun currentAllAudioBooksSortOrder() =
        sortingAudioBookSelectOptionActionHandler.toOrder()

    private fun currentAllUsersOrder() = sortingUsersSelectOptionActionHandler.toOrder()

    private fun updateSearchQuery(searchString: String) = searchStringFlow.tryEmit(searchString)

    fun deleteBookInSavedBooks(id: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { savedBooksRepository.deleteBookInSavedBooks(id) },
            onSuccess = { emitToShowSuccessNotificationFlow(IdResourceString(R.string.book_removed_successfully)) },
            onError = { emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it)) }
        )
    }

    private fun addSearchViewAdapterModel(items: List<Item>): List<Item> {
        val newItems = mutableListOf<Item>()
        newItems.add(0, SearchAdapterModel(listener = this))
        newItems.addAll(items)
        return newItems
    }

    private fun applySavedBookSearchFiltered(
        items: List<BookThatReadDomain>,
        searchString: String
    ) =
        if (searchString.isBlank()) items
        else items.filter { it.title.contains(searchString, ignoreCase = true) }

    private fun applyBookSearchFiltered(
        items: List<BookAdapterModel>,
        searchString: String
    ) =
        if (searchString.isBlank()) items
        else items.filter { it.title.contains(searchString, ignoreCase = true) }

    private fun applyAudioBookSearchFiltered(
        items: List<AudioBookAdapterModel>,
        searchString: String
    ) =
        if (searchString.isBlank()) items
        else items.filter { it.audioBook.title.contains(searchString, ignoreCase = true) }

    private fun applyAllUsersSearchFiltered(
        items: List<UserAdapterModel>,
        searchString: String
    ) =
        if (searchString.isBlank()) items
        else items.filter { it.fullName().contains(searchString, ignoreCase = true) }

    private fun applyAllGenresSearchFiltered(
        items: List<BookGenreItem>,
        searchString: String
    ) = if (searchString.isBlank()) items else items.filter { it.titles.contains(searchString) }

    private fun currentAllSavedOrder() = sortingSavedBookSelectOptionActionHandler.toOrder()

    private fun mapSavedBooksToAdapterModel(books: List<BookThatReadDomain>) = books
        .map(savedBookDomainToBookMapper::map)
        .map { savedBook -> SavedBookAdapterModel(savedBook = savedBook, listeners = this) }


    private fun startFetchAllBooks(parameters: Pair<Pair<String, String>, BookSortOrder>) =
        fetchAllSortedBooksUseCase(
            userId = parameters.first.first,
            schoolId = parameters.first.second,
            order = parameters.second
        )

    private fun mapBooksDomainToBook(books: List<BookDomain>) =
        books.map(bookDomainToBookMapper::map)

    private fun mapBooksToAdapterItems(books: List<Book>) = books
        .map { book ->
            BookAdapterModel(
                id = book.id,
                posterUrl = book.poster.url,
                title = book.title,
                author = book.author,
                publishYear = book.publicYear,
                pageCount = book.page,
                savedStatus = book.savedStatus, listener = this
            )
        }

    override fun bookItemOnClick(bookId: String) {
        navigate(fragmentAllBooksRouter.navigateToFragmentDetailsFragment(bookId = bookId))
    }

    override fun bookOptionMenuOnClick(bookId: String) {
        _showBookOptionDialogFlow.tryEmit(bookId)
    }

    override fun genreItemOnClickListeners(genreCode: String) {
        navigate(fragmentAllGenresRouter.navigateRoGenreInfoFragment(genreCode))
    }

    override fun audioBookItemOnClick(audioBookId: String) {
        _playAudioBookFlow.tryEmit(audioBookId)
    }

    override fun userItemOnClickListener(userId: String) {

    }

    override fun bookGenreOnClickListener(id: String) {
        navigate(fragmentAllGenresRouter.navigateRoGenreInfoFragment(id))
    }

    override fun genreBlockButtonOnClickListener() = Unit

    override fun saveBookItemOnClick(savedBook: BookThatRead) {
        navigate(
            router.navigateToBookChaptersFragment(
                path = checkAndGetSavedBookRath(savedBook),
                savedBook = savedBook
            )
        )
    }

    private fun checkAndGetSavedBookRath(savedBook: BookThatRead): String {
        val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(savedBook.bookId)
        return if (savedBookPath == null) {
            emitToErrorMessageFlow(IdResourceString(R.string.book_is_not_ready))
            String()
        } else savedBookPath
    }

    override fun saveBookItemOnLongClick(savedBookId: String) {
        _showConfirmDialogFlow.tryEmit(savedBookId)
    }

    override fun onQueryTextSubmit(searchString: String?): Boolean {
        if (searchString != null) updateSearchQuery(searchString = searchString)
        return false
    }

    override fun onQueryTextChange(searchString: String?): Boolean {
        if (searchString != null) updateSearchQuery(searchString = searchString)
        return false
    }

    private fun <T> createMutableSharedFlowAsSingleLiveEvent(): MutableSharedFlow<T> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)


    private fun emitToErrorMessageFlow(messageId: IdResourceString) =
        _isErrorMessageIdFlow.tryEmit(messageId)

    private fun emitToShowSuccessNotificationFlow(messageId: IdResourceString) =
        _showSuccessSnackbarFlow.tryEmit(messageId)

    private fun emitToErrorMessageFlow(message: String) = _isErrorMessageFlow.tryEmit(message)

    private fun navigate(navCommand: NavCommand) = _navCommand.tryEmit(navCommand)

    companion object {
        const val SEARCH_DEBOUNCE = 300L
    }
}


