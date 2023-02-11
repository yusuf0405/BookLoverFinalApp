package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog

import com.example.domain.sort_dialog.BookSortOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortingBookSelectOptionActionHandler @Inject constructor() :
    SelectOptionActionHandler<SortingBookOptionsMenuActions>(SortingBookOptionsMenuActions.OrderByBookName) {

    fun toOrder(): BookSortOrder =
        when (currentState) {
            is SortingBookOptionsMenuActions.OrderByDate -> BookSortOrder.BY_DATE
            is SortingBookOptionsMenuActions.OrderByCached -> BookSortOrder.BY_CACHED
            is SortingBookOptionsMenuActions.OrderByBookName -> BookSortOrder.BY_BOOK_NAME
            is SortingBookOptionsMenuActions.OrderByAuthorName -> BookSortOrder.BY_AUTHOR_NAME
        }
}

