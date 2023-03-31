package com.example.bookloverfinalapp.app.ui.general_screens.screen_main

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Collections
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.UploadFileType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers.MainItemsToSearchFilteredModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AddNewBooksItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.MainScreenErrorItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.SelectFavoriteBooksItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.router.FragmentMainScreenRouter
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.MainScreenItems
import com.example.domain.models.UserDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.UserCacheRepository
import com.example.domain.use_cases.FetchAllMainScreenItemsUseCase
import com.joseph.stories.presentation.models.StoriesItemOnClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentMainScreenViewModel @Inject constructor(
    fetchAllMainScreenItemsUseCase: FetchAllMainScreenItemsUseCase,
    userCacheRepository: UserCacheRepository,
    private val router: FragmentMainScreenRouter,
    private val dispatchersProvider: DispatchersProvider,
    private val savedBooksRepository: BookThatReadRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val itemsToSearchFilteredModelMapper: MainItemsToSearchFilteredModelMapper,
    private val resourceProvider: ResourceProvider,
    userDomainToUserMapper: Mapper<UserDomain, User>
) : BaseViewModel(), BookItemOnClickListener, MainScreenOpenMoreClickListeners,
    GenreItemOnClickListeners, ErrorItemOnClickListener, UserItemOnClickListener,
    SavedBookItemOnClickListeners, AudioBookItemOnClickListener, BookGenreItemOnClickListeners,
    StoriesItemOnClickListener, CollectionItemOnClickListener,
    SelectFavoriteBooksItemOnClickListeners, AddNewBooksItemOnClickListeners {

    private val _showConfirmDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showConfirmDialogFlow get() = _showConfirmDialogFlow.asSharedFlow()

    private val _playAudioBookFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val playAudioBookFlow get() = _playAudioBookFlow.asSharedFlow()

    private val _showBookOptionDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showBookOptionDialogFlow get() = _showBookOptionDialogFlow.asSharedFlow()

    private val _showAddStoriesDialogFlow = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val showAddStoriesDialogFlow get() = _showAddStoriesDialogFlow.asSharedFlow()

    private val recyclerViewStateFlow = MutableStateFlow<Parcelable?>(null)

    private var _motionPosition = MutableStateFlow(0f)
    val motionPosition get() = _motionPosition.asStateFlow()

    val currentUserFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .flowOn(Dispatchers.IO)
        .map(userDomainToUserMapper::map)
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, User.unknown())

    val allFilteredItemsFlow = fetchAllMainScreenItemsUseCase()
        .map { items -> mapToAdapterModel(items) }
        .onStart {
//            emit(listOf(MainScreenShimmerItem))
        }
        .flowOn(Dispatchers.Default)
        .catch { exception: Throwable ->
            Log.i("Joseph", exception.toString())
            handleError(exception)
//            emit(createErrorAdapterModel(exception))
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun updateMotionPosition(position: Float) = _motionPosition.tryEmit(position)


    fun saveRecyclerViewCurrentState(state: Parcelable?) = recyclerViewStateFlow.tryEmit(state)

    fun fetchRecyclerViewCurrentState(): Parcelable? = recyclerViewStateFlow.value

    fun navigateUploadBookFragment(uploadType: UploadFileType) =
        navigate(router.navigateToUploadBookFragment(uploadType))

    fun navigateToSearchFragment() = navigate(router.navigateToSearchFragment())

    fun navigateToCreateQuestionFragment(bookId: String) {
        navigate(router.navigateToCreateQuestionNavGraph(bookId = bookId))
    }

    fun navigateToEditBookFragment(bookId: String) {
        navigate(router.navigateToEditBookFragment(bookId = bookId))
    }

    fun navigateToProfileFragment() {
        navigate(router.navigateToProfileFragment())
    }

    fun deleteBookInSavedBooks(id: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { savedBooksRepository.deleteBookInSavedBooks(id) },
            onSuccess = { emitToShowSuccessNotificationFlow(IdResourceString(R.string.book_removed_successfully)) },
            onError = { emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it)) }
        )
    }

    private fun mapToAdapterModel(items: MainScreenItems) =
        itemsToSearchFilteredModelMapper.map(
            items = items,
            bookItemOnClickListener = this,
            audioBookItemOnClickListener = this,
            openMoreClickListeners = this,
            userItemOnClickListener = this,
            savedBookItemOnClickListener = this,
            genreItemOnClickListeners = this,
            bookGenreItemOnClickListeners = this,
            storiesOnClickListener = this,
            collectionItemOnClickListener = this,
            selectFavoriteBooksItemOnClickListener = this,
            addNewBooksItemOnClickListener = this,
        )

    private fun handleError(exception: Throwable) {
        emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(exception))
    }

    private fun createErrorAdapterModel(exception: Throwable) = listOf(
        MainScreenErrorItem(
            resourceProvider.fetchErrorMessage(exception),
            listener = this@FragmentMainScreenViewModel
        )
    )

    override fun bookItemOnClick(bookId: String) {
        navigate(router.navigateToBookDetailsFragment(bookId = bookId))
    }

    override fun bookOptionMenuOnClick(bookId: String) {
        _showBookOptionDialogFlow.tryEmit(bookId)
    }

    override fun navigateToAllBooksFragment() {
        navigate(router.navigateToAllBooksFragment())
    }

    override fun navigateToAllTasksFragment() {
        navigate(router.navigateToAllTasksFragment())
    }

    override fun navigateToAllAudioBooksFragment() {
        navigate(router.navigateToAllAudioBooksFragment())

    }

    override fun navigateToAllSavedBooksFragment() {
        navigate(router.navigateToAllSavedBooksFragment())
    }

    override fun navigateToAllStudentsFragment() {
        navigate(router.navigateToAllStudentsFragment())
    }

    override fun navigateToAllGenresFragment() {
        navigate(router.navigateToAllChaptersFragment())
    }

    override fun tryAgain() {
    }

    override fun saveBookItemOnClick(savedBook: BookThatRead) {
        navigateToBookChaptersFragment(savedBook)
    }

    fun navigateToBookChaptersFragment(savedBook: BookThatRead) {
        val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(savedBook.bookId)
        if (savedBookPath == null) emitToErrorMessageFlow(IdResourceString(R.string.book_is_not_ready))
        else navigate(
            router.navigateToBookChaptersFragment(
                path = savedBookPath,
                savedBook = savedBook
            )
        )
    }

    override fun saveBookItemOnLongClick(savedBookId: String) {
        _showConfirmDialogFlow.tryEmit(savedBookId)
    }

    override fun genreItemOnClickListeners(genreCode: String) {
        navigate(router.navigateToGenreInfoFragment(genreCode))
    }

    override fun userItemOnClickListener(userId: String) {
        navigate(router.navigateToUserInfoFragment(userId = userId))
    }

    override fun audioBookItemOnClick(audioBookId: String) {
        _playAudioBookFlow.tryEmit(audioBookId)
    }

    override fun bookGenreOnClickListener(id: String) {
        navigate(router.navigateToGenreInfoFragment(id))
    }

    override fun genreBlockButtonOnClickListener() = Unit

    override fun storiesOnClickListener(position: Int) {
        navigate(router.navigateToStoriesFragment(position))
    }

    override fun addStoriesOnClickListener() {
        _showAddStoriesDialogFlow.tryEmit(Unit)
    }

    override fun collectionItemOnClick(collections: Collections) {
        when (collections) {
            Collections.ALL_BOOKS -> navigateToAllBooksFragment()
            Collections.ALL_AUDIO_BOOKS -> navigateToAllAudioBooksFragment()
            Collections.SAVED_BOOKS -> navigateToAllSavedBooksFragment()
            Collections.GENRES -> navigateToAllGenresFragment()
            Collections.USERS -> navigateToAllStudentsFragment()
        }
    }

    override fun onClickSelectFavoriteBook() {
        val destination = router.navigateToSelectFavoriteBookFragment()
        navigate(destination)
    }

    override fun onClickAddNewBook() {

    }
}
