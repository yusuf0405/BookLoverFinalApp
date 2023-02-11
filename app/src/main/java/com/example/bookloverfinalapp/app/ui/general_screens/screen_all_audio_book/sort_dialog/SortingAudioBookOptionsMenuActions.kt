package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.OptionMenuAction

/**
 * Класс, который содержит различные действия,
 * которые можно выполнить при выборе опции в меню сортировки
 */
sealed class SortingAudioBookOptionsMenuActions : OptionMenuAction {

    object OrderByDate : SortingAudioBookOptionsMenuActions()

    object OrderByUsersSortName : SortingAudioBookOptionsMenuActions()

    object OrderByAuthorName : SortingAudioBookOptionsMenuActions()
}