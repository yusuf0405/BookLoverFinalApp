package com.example.bookloverfinalapp.app.ui.general_screens.screen_search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.mappers.ItemsToSearchFilteredModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.router.FragmentSearchRouter
import com.example.data.cache.models.IdResourceString
import com.example.domain.models.MainScreenItems
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.domain.use_cases.FetchAllMainScreenItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentSearchViewModel @Inject constructor(
    fetchAllMainScreenItemsUseCase: FetchAllMainScreenItemsUseCase,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val itemsToSearchFilteredModelMapper: ItemsToSearchFilteredModelMapper,
    private val router: FragmentSearchRouter
) : BaseViewModel(), BookItemOnClickListener, AudioBookItemOnClickListener,
    UserItemOnClickListener, BookGenreItemOnClickListeners, SavedBookItemOnClickListeners {

    private val searchStringFlow = MutableStateFlow(String())

    private val _showBookOptionDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showBookOptionDialogFlow get() = _showBookOptionDialogFlow.asSharedFlow()

    private val _playAudioBookFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val playAudioBookFlow get() = _playAudioBookFlow.asSharedFlow()

    val allFilteredItemsFlow = fetchAllMainScreenItemsUseCase()
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchString -> mapToAdapterModel(items, searchString) }
        .onStart {
//            emit(listOf(MainScreenShimmerItem))
        }
        .flowOn(Dispatchers.Default)
        .catch { exception: Throwable ->
            Log.i("Joseph", exception.toString())
//            handleError(exception)
//            emit(createErrorAdapterModel(exception))
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(searchString: String) = searchStringFlow.tryEmit(searchString)

    private fun mapToAdapterModel(items: MainScreenItems, searchString: String) =
        itemsToSearchFilteredModelMapper.map(
            items = items,
            searchQuery = searchString,
            bookItemOnClickListener = this,
            audioBookItemOnClickListener = this,
            userItemOnClickListener = this,
            savedBookItemOnClickListener = this,
            bookGenreItemOnClickListeners = this
        )

    override fun saveBookItemOnClick(savedBook: BookThatRead) {
        navigateToBookReadBlockFragment(savedBook)
    }

    fun navigateToBookReadBlockFragment(savedBook: BookThatRead) {
        val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(savedBook.bookId)
        if (savedBookPath == null) emitToErrorMessageFlow(IdResourceString(R.string.book_is_not_ready))
        else navigate(router.navigateToBookReadBlockFragment(savedBookPath, savedBook))
    }

    fun navigateToEditBookFragment(bookId: String) {
        navigate(router.navigateToEditBookFragment(bookId))
    }

    fun navigateToCreateQuestionFragment(bookId: String) {
        navigate(router.navigateToCreateQuestionFragment(bookId))
    }

    override fun userItemOnClickListener(userId: String) {
        navigate(router.navigateToUserInfoFragment(userId))
    }

    override fun audioBookItemOnClick(audioBookId: String) {
        _playAudioBookFlow.tryEmit(audioBookId)
    }

    override fun bookItemOnClick(bookId: String) {
        navigate(router.navigateToBookInfoFragment(bookId = bookId))
    }

    override fun bookOptionMenuOnClick(bookId: String) {
        _showBookOptionDialogFlow.tryEmit(bookId)
    }

    override fun bookGenreOnClickListener(id: String) = Unit

    override fun genreBlockButtonOnClickListener() = Unit
}