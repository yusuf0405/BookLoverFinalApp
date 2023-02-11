package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.SelectOptionActionHandler
import com.example.domain.sort_dialog.SavedBookSortOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortingSavedBookSelectOptionActionHandler @Inject constructor() :
    SelectOptionActionHandler<SortingSavedBookOptionsMenuActions>(SortingSavedBookOptionsMenuActions.OrderByUsersSortName) {

    fun toOrder() = when (currentState) {
        is SortingSavedBookOptionsMenuActions.OrderByDate -> SavedBookSortOrder.BY_DATE
        is SortingSavedBookOptionsMenuActions.OrderByUsersSortName -> SavedBookSortOrder.BY_BOOK_NAME
        is SortingSavedBookOptionsMenuActions.OrderByAuthorName -> SavedBookSortOrder.BY_AUTHOR_NAME
        is SortingSavedBookOptionsMenuActions.OrderByReading -> SavedBookSortOrder.BY_READING
    }
}

