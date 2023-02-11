package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SortingAudioBookOptionsViewModel @Inject constructor(
    private val selectOptionActionHandler: SortingAudioBookSelectOptionActionHandler
) : ViewModel() {

    val internalSelection = MutableStateFlow<SortingAudioBookOptionsMenuActions>(
        SortingAudioBookOptionsMenuActions.OrderByDate
    )

    init {
        internalSelection.tryEmit(selectOptionActionHandler.currentState)
    }

    fun orderSavedBooks(action: SortingAudioBookOptionsMenuActions) {
        selectOptionActionHandler.trySelectAction(action)
    }
}