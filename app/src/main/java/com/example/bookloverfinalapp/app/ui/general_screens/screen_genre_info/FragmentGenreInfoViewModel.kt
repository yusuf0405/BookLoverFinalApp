package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info

import androidx.lifecycle.viewModelScope
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.mappers.GenreItemsToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.router.FragmentGenreInfoRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.AudioBookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.EmptyDataItem
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.data.cache.models.IdResourceString
import com.example.domain.repository.GenresRepository
import com.example.domain.use_cases.FetchSimilarBooksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class FragmentGenreInfoViewModel @AssistedInject constructor(
    @Assisted private val genreId: String,
    private val fetchSimilarBooksUseCase: FetchSimilarBooksUseCase,
    private val genresRepository: GenresRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val genreItemsToAdapterModelMapper: GenreItemsToAdapterModelMapper,
    private val router: FragmentGenreInfoRouter,
) : BaseViewModel(), AudioBookItemOnClickListener,
    BookItemOnClickListener {

    private val genreIdFlow = MutableStateFlow(genreId)
    val genre = genreIdFlow.map(genresRepository::fetchGenreFromCache)

    private val _playAudioBookFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val playAudioBookFlow get() = _playAudioBookFlow.asSharedFlow()

    private val _showBookOptionDialogFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val showBookOptionDialogFlow get() = _showBookOptionDialogFlow.asSharedFlow()

    fun fetchGenreBooks(genreCode: String) =
        fetchSimilarBooksUseCase(genresId = listOf(genreCode), bookId = String()).map { items ->
            genreItemsToAdapterModelMapper.map(
                items = items,
                searchQuery = String(),
                audioBookItemOnClickListener = this,
                bookOnClickListeners = this
            )
        }.map {
            if (it.size <= 2) listOf(EmptyDataItem)
            else it
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun navigateToEditBookFragment(bookId: String) {
        navigate(router.navigateToEditBookFragment(bookId))
    }

    fun navigateToCreateQuestionFragment(bookId: String) {
        navigate(router.navigateToCreateQuestionFragment(bookId))
    }

    fun navigateToBookReadFragment(savedBook: BookThatRead) {
        val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(savedBook.bookId)
        if (savedBookPath == null) emitToErrorMessageFlow(IdResourceString(R.string.book_is_not_ready))
        else navigate(
            router.navigateToBookReadFragment(path = savedBookPath, savedBook = savedBook)
        )
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

    @AssistedFactory
    interface Factory {
        fun create(
            genreId: String
        ): FragmentGenreInfoViewModel
    }
}