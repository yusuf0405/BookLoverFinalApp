package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.OptionMenuAction

/**
 * Класс, который содержит различные действия,
 * которые можно выполнить при выборе опции в меню сортировки
 */
sealed class SortingUsersOptionsMenuActions : OptionMenuAction {

    object OrderByUserName : SortingUsersOptionsMenuActions()

    object OrderByUserLastName : SortingUsersOptionsMenuActions()

    object OrderByMoreBooks : SortingUsersOptionsMenuActions()

    object OrderByMoreChapters : SortingUsersOptionsMenuActions()

    object OrderByMorePages : SortingUsersOptionsMenuActions()

}