package com.example.data.cloud

import com.example.domain.Mapper

sealed class CloudDataRequestState<T> {

    class Success<T>(val data: T) : CloudDataRequestState<T>()

    class Error<T>(val error: Throwable) : CloudDataRequestState<T>()

    fun <R> map(mapper: Mapper<T, R>? = null): CloudDataRequestState<R> = when (this) {
        is Error -> Error(this.error)
        is Success -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success result")
            else Success(mapper.map(this.data))
        }
    }
}

fun <T> CloudDataRequestState<T>?.takeSuccess(): T? =
    if (this is CloudDataRequestState.Success) data else null


fun <T> CloudDataRequestState<T>?.takeError(): Throwable? =
    if (this is CloudDataRequestState.Error) error else null