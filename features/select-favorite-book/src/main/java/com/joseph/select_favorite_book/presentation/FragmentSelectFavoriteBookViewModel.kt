package com.joseph.select_favorite_book.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.joseph.common.base.BaseViewModel
import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import com.joseph.select_favorite_book.domain.usecase.FetchSelectFavoriteBooksUseCase
import com.joseph.select_favorite_book.presentation.adapter.models.SelectFavoriteBookItem
import com.joseph.select_favorite_book.presentation.mappers.SelectBookToAdapterItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class FragmentSelectFavoriteBookUiState(
    val items: List<SelectFavoriteBookItem> = emptyList()
)

@HiltViewModel
internal class FragmentSelectFavoriteBookViewModel @Inject constructor(
    private val fetchSelectFavoriteBooksUseCase: FetchSelectFavoriteBooksUseCase,
    private val selectBookToAdapterItemMapper: SelectBookToAdapterItemMapper,
) : BaseViewModel() {

    var uiState by mutableStateOf(FragmentSelectFavoriteBookUiState())

    private val currentShowedItemPositionFlow = MutableStateFlow(0)

    val selectBooksFlow = fetchSelectFavoriteBooksUseCase()
        .catch { throwable -> handleError(throwable) }
        .map(::mapToAdapterModel)

        .onEach {
            Log.i("Joseph", "size = ${it.map { it.posterUrl }}")
            uiState = uiState.copy(items = it)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

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