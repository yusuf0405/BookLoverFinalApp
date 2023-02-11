package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SortingUsersOptionsViewModel @Inject constructor(
    private val selectOptionActionHandler: SortingUsersSelectOptionActionHandler
) : ViewModel() {

    val internalSelection = MutableStateFlow<SortingUsersOptionsMenuActions>(
        SortingUsersOptionsMenuActions.OrderByUserName
    )

    init {
        internalSelection.tryEmit(selectOptionActionHandler.currentState)
    }

    fun orderUsers(action: SortingUsersOptionsMenuActions) {
        selectOptionActionHandler.trySelectAction(action)
    }
}