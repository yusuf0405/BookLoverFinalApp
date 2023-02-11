package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.select_value_option

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


interface Value

abstract class SelectValueandler<T : Value>(initialValue: T) {

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