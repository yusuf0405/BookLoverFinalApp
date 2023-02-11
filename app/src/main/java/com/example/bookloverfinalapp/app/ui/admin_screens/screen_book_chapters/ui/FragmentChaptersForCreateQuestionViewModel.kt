package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.ui

import android.util.Log
import android.widget.SearchView
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter.QuesionChapterItemOnClickListener
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter.QuestionChapterAdapterModel
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.router.FragmentChaptersForCreateQuestionRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.SearchAdapterModel
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.BookDomain
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.BooksSaveToFileRepository
import com.shockwave.pdfium.PdfDocument
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class FragmentChaptersForCreateQuestionViewModel constructor(
    private val bookId: String,
    private val booksRepository: BooksRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val router: FragmentChaptersForCreateQuestionRouter,
    private val dispatchersProvider: DispatchersProvider,
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>
) : BaseViewModel(), SearchView.OnQueryTextListener, QuesionChapterItemOnClickListener {

    private val searchStringFlow = MutableStateFlow(String())
    private val currentChapterFlow = MutableStateFlow(0)
    private val internalChaptersFlow =
        MutableStateFlow<List<QuestionChapterAdapterModel>>(emptyList())

    private val _showChoiceCreateQuestionDialogFlow =
        createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val showChoiceCreateQuestionDialogFlow get() = _showChoiceCreateQuestionDialogFlow.asSharedFlow()

    val bookFlow = flow { emit(booksRepository.fetchBookFromIdInCache(bookId)) }
        .map(bookDomainToBookMapper::map)
        .flowOn(dispatchersProvider.default())
        .stateIn(viewModelScope, SharingStarted.Lazily, Book.unknown())

    val bookPatchFlow = bookFlow.map { it.id }
        .map(booksSaveToFileRepository::fetchSavedBookFilePath)
        .onEach(::handleBookPatchFetching)
        .flowOn(dispatchersProvider.default())

    val bookChaptersFlow = internalChaptersFlow
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchQuery -> applySearchFiltered(items, searchQuery) }
        .map(::addSearchViewModelToAdapter)
        .flowOn(dispatchersProvider.default())

    fun navigateToCreateQuestionFragment() {
        navigate(
            router.navigateNavigateToCreateQuestionFragment(
                bookId = bookId,
                chapter = currentChapterFlow.value
            )
        )
    }

    private fun handleBookPatchFetching(patch: String?) {
        if (!patch.isNullOrEmpty() && bookFlow.value == Book.unknown()) return
        val chapters = mutableListOf<QuestionChapterAdapterModel>()
        for (i in 1..bookFlow.value.chapterCount) {
            val item = QuestionChapterAdapterModel(
                title = i.toString(),
                chapter = i,
                actionListener = this
            )
            chapters.add(item)
        }
        internalChaptersFlow.tryEmit(chapters)
    }

    private fun addSearchViewModelToAdapter(items: List<Item>): List<Item> {
        val newItems = mutableListOf<Item>()
        newItems.add(SearchAdapterModel(listener = this))
        newItems.addAll(items)
        return newItems
    }

    private fun applySearchFiltered(
        items: List<QuestionChapterAdapterModel>,
        searchString: String
    ) = if (searchString.isBlank()) items
    else items.filter { it.title.contains(searchString, ignoreCase = true) }

    private fun updateSearchQuery(searchString: String) = searchStringFlow.tryEmit(searchString)

    fun updateBookChapters(chapters: List<PdfDocument.Bookmark>) {
        val adapterModels = chapters.mapIndexed { index, item ->
            QuestionChapterAdapterModel(
                title = item.title,
                chapter = index + 1,
                actionListener = this
            )
        }
        internalChaptersFlow.tryEmit(adapterModels)
    }

    override fun onQueryTextSubmit(searchString: String?): Boolean {
        if (searchString != null) updateSearchQuery(searchString = searchString)
        return false
    }

    override fun onQueryTextChange(searchString: String?): Boolean {
        if (searchString != null) updateSearchQuery(searchString = searchString)
        return false
    }

    override fun chapterItemOnClickListener(currentChapterPosition: Int) {
        currentChapterFlow.tryEmit(currentChapterPosition)
        _showChoiceCreateQuestionDialogFlow.tryEmit(Unit)
    }
}