package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog

/**
 * Класс, который содержит различные действия,
 * которые можно выполнить при выборе опции в меню сортировки
 */
sealed class SortingBookOptionsMenuActions : OptionMenuAction {

    object OrderByDate : SortingBookOptionsMenuActions()

    object OrderByCached : SortingBookOptionsMenuActions()

    object OrderByBookName : SortingBookOptionsMenuActions()

    object OrderByAuthorName : SortingBookOptionsMenuActions()
}