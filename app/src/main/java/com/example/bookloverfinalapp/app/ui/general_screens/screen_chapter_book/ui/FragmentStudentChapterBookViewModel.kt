package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.ui

import android.widget.SearchView
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter.ChapterAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter.ChapterItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.router.FragmentChapterBookRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.SearchAdapterModel
import com.shockwave.pdfium.PdfDocument.Bookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class FragmentStudentChapterBookViewModel(
    private val book: BookThatRead,
    private val patch: String,
    private val router: FragmentChapterBookRouter
) : BaseViewModel(), SearchView.OnQueryTextListener, ChapterItemOnClickListener {

    private val searchStringFlow = MutableStateFlow(String())
    private val _allBookChapters = MutableStateFlow<List<Bookmark>>(emptyList())
    private val bookLastPageFlow = MutableStateFlow(0)

    val allBookChapters = _allBookChapters.map(::mapToAdapterModels)
        .combine(searchStringFlow.debounce(SEARCH_DEBOUNCE))
        { items, searchQuery -> applySearchFiltered(items, searchQuery) }
        .map(::addSearchViewAdapterModel)
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun updateChapters(items: List<Bookmark>) = _allBookChapters.tryEmit(items)

    fun updateBookLastPage(page: Int) = bookLastPageFlow.tryEmit(page)

    private fun mapToAdapterModels(chapters: List<Bookmark>) =
        chapters.mapIndexed { index, bookmark ->
            ChapterAdapterModel(
                chapter = bookmark,
                actionListener = this,
                chapterIsRead = book.isReadingPages[index],
                currentChapterPosition = index + 1
            )
        }

    private fun applySearchFiltered(items: List<ChapterAdapterModel>, searchString: String) =
        if (searchString.isBlank()) items
        else items.filter { it.chapter.title.contains(searchString, ignoreCase = true) }

    private fun addSearchViewAdapterModel(items: List<ChapterAdapterModel>): List<Item> {
        val newItems = mutableListOf<Item>()
        newItems.add(0, SearchAdapterModel(listener = this))
        newItems.addAll(items)
        return newItems
    }

    private fun updateSearchQuery(searchString: String) = searchStringFlow.tryEmit(searchString)

    override fun onQueryTextSubmit(searchString: String?): Boolean {
        if (searchString != null) updateSearchQuery(searchString = searchString)
        return false
    }

    override fun onQueryTextChange(searchString: String?): Boolean {
        if (searchString != null) updateSearchQuery(searchString = searchString)
        return false
    }

    override fun chapterItemOnClickListener(chapter: Bookmark, currentChapterPosition: Int) {
        val lastPage =
            if (currentChapterPosition == _allBookChapters.value.size) bookLastPageFlow.value
            else _allBookChapters.value[currentChapterPosition].pageIdx.toInt()
        navigate(
            router.navigateToReaderFragment(
                book = book,
                chapter = currentChapterPosition,
                startPage = chapter.pageIdx.toInt(),
                lastPage = lastPage,
                path = patch,
                chapterTitle = chapter.title
            )
        )
    }

    fun navigate(){
        navigate(
            router.navigateToReaderFragment(
                book = book,
                chapter = 1,
                startPage = 0,
                lastPage = 9999,
                path = patch,
                chapterTitle = book.title
            )
        )
    }

    private companion object {
        const val SEARCH_DEBOUNCE = 300L
    }
}