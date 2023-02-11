package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.OptionMenuAction

/**
 * Класс, который содержит различные действия,
 * которые можно выполнить при выборе опции в меню сортировки
 */
sealed class SortingSavedBookOptionsMenuActions : OptionMenuAction {

    object OrderByDate : SortingSavedBookOptionsMenuActions()

    object OrderByReading : SortingSavedBookOptionsMenuActions()

    object OrderByUsersSortName : SortingSavedBookOptionsMenuActions()

    object OrderByAuthorName : SortingSavedBookOptionsMenuActions()
}