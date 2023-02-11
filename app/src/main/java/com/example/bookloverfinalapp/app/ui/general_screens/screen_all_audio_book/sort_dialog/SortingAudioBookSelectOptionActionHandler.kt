package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.SelectOptionActionHandler
import com.example.domain.sort_dialog.AudioBookSortOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortingAudioBookSelectOptionActionHandler @Inject constructor() :
    SelectOptionActionHandler<SortingAudioBookOptionsMenuActions>(SortingAudioBookOptionsMenuActions.OrderByUsersSortName) {

    fun toOrder() = when (currentState) {
        is SortingAudioBookOptionsMenuActions.OrderByDate -> AudioBookSortOrder.BY_DATE
        is SortingAudioBookOptionsMenuActions.OrderByUsersSortName -> AudioBookSortOrder.BY_BOOK_NAME
        is SortingAudioBookOptionsMenuActions.OrderByAuthorName -> AudioBookSortOrder.BY_AUTHOR_NAME
    }
}

