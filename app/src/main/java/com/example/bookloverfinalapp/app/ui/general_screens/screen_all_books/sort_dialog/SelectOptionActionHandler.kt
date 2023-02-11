package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


interface OptionMenuAction

abstract class SelectOptionActionHandler<T : OptionMenuAction>(initialValue: T) {

    private var state: T
    private val internalSelection = MutableSharedFlow<T>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val currentState get() = state

    init {
        state = initialValue
        internalSelection.tryEmit(initialValue)
    }

    fun selectAction(action: T) {
        state = action
        internalSelection.tryEmit(action)
    }

    fun trySelectAction(action: T) {
        if (state != action) {
            state = action
            internalSelection.tryEmit(action)
        }
    }

    fun observeSelections() = internalSelection.asSharedFlow()

}