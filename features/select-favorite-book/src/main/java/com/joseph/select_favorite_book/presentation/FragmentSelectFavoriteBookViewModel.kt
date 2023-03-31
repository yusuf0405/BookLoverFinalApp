package com.joseph.select_favorite_book.presentation

import androidx.lifecycle.viewModelScope
import com.joseph.common_api.base.BaseViewModel
import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import com.joseph.select_favorite_book.domain.usecase.FetchSelectFavoriteBooksUseCase
import com.joseph.select_favorite_book.presentation.mappers.SelectBookToAdapterItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class FragmentSelectFavoriteBookViewModel @Inject constructor(
    private val fetchSelectFavoriteBooksUseCase: FetchSelectFavoriteBooksUseCase,
    private val selectBookToAdapterItemMapper: SelectBookToAdapterItemMapper,
) : BaseViewModel() {

    private val currentShowedItemPositionFlow = MutableStateFlow(0)

    val selectBooksFlow = fetchSelectFavoriteBooksUseCase()
        .catch { throwable -> handleError(throwable) }
        .map(::mapToAdapterModel)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val posterUrlFlow = selectBooksFlow.combine(currentShowedItemPositionFlow)
    { books, position -> Pair(books, position) }
        .filter { it.first.isNotEmpty() }
        .map { it.first[it.second].posterUrl }
        .catch { }

    fun setCurrentShowedItemPosition(position: Int) {
        currentShowedItemPositionFlow.tryEmit(position)
    }

    private fun mapToAdapterModel(books: List<SelectFavoriteBook>) =
        books.map(selectBookToAdapterItemMapper::map)

    private fun handleError(throwable: Throwable) {

    }
}