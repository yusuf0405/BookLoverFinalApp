package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.SelectOptionActionHandler
import com.example.domain.sort_dialog.StudentSortOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortingUsersSelectOptionActionHandler @Inject constructor() :
    SelectOptionActionHandler<SortingUsersOptionsMenuActions>(SortingUsersOptionsMenuActions.OrderByUserName) {

    fun toOrder() = when (currentState) {
        is SortingUsersOptionsMenuActions.OrderByUserName -> StudentSortOrder.BY_USER_NAME
        is SortingUsersOptionsMenuActions.OrderByUserLastName -> StudentSortOrder.BY_LAST_NAME
        is SortingUsersOptionsMenuActions.OrderByMoreBooks -> StudentSortOrder.BY_READ_MORE_BOOKS
        is SortingUsersOptionsMenuActions.OrderByMoreChapters -> StudentSortOrder.BY_READ_MORE_CHAPTERS
        is SortingUsersOptionsMenuActions.OrderByMorePages -> StudentSortOrder.BY_READ_MORE_PAGES
    }
}

