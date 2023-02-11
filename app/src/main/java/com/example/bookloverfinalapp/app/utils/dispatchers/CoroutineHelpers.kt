package com.example.bookloverfinalapp.app.utils.dispatchers

import com.example.domain.RequestState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers

inline fun <T> CoroutineScope.launchSafe(
    crossinline safeAction: suspend () -> RequestState<T>,
    crossinline onError: suspend (Throwable) -> Unit,
    crossinline onSuccess: (T) -> Unit,
    dispatcher: CoroutineDispatcher,
    errorDispatcher: CoroutineDispatcher = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
): Job {
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        launch(context = errorDispatcher, start = start) { onError(exception) }
    }
    return this.launch(context = exceptionHandler + dispatcher) {
        when (val result = safeAction()) {
            is RequestState.Success -> onSuccess(result.data)
            is RequestState.Error -> onError(result.error)
        }
    }
}