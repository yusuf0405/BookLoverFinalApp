package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SortingBookOptionsViewModel @Inject constructor(
    private val selectOptionActionHandler: SortingBookSelectOptionActionHandler
) : ViewModel() {

    val internalSelection = MutableStateFlow<SortingBookOptionsMenuActions>(
        SortingBookOptionsMenuActions.OrderByDate
    )

    init {
        internalSelection.tryEmit(selectOptionActionHandler.currentState)
    }

    fun orderBooks(action: SortingBookOptionsMenuActions) {
        selectOptionActionHandler.trySelectAction(action)
    }
}