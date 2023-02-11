package com.example.bookloverfinalapp.app.utils.extensions

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.stateIn

fun <T> createSharedFlowAsLiveData() = MutableSharedFlow<T>(
    replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
)

fun <T> createSharedFlowAsSingleLiveEvent(): MutableSharedFlow<T> =
    MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )