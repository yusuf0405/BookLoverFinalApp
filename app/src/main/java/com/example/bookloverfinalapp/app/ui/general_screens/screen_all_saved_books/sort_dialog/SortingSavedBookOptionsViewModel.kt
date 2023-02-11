package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SortingSavedBookOptionsViewModel @Inject constructor(
    private val selectOptionActionHandler: SortingSavedBookSelectOptionActionHandler
) : ViewModel() {

    val internalSelection = MutableStateFlow<SortingSavedBookOptionsMenuActions>(
        SortingSavedBookOptionsMenuActions.OrderByDate
    )

    init {
        internalSelection.tryEmit(selectOptionActionHandler.currentState)
    }

    fun orderSavedBooks(action: SortingSavedBookOptionsMenuActions) {
        selectOptionActionHandler.trySelectAction(action)
    }
}