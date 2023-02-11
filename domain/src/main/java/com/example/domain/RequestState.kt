package com.example.domain

sealed class RequestState<T> {

    class Success<T>(val data: T) : RequestState<T>()

    class Error<T>(val error: Throwable) : RequestState<T>()

    fun <R> map(mapper: Mapper<T, R>? = null): RequestState<R> = when (this) {
        is Error -> Error(this.error)
        is Success -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success result")
            else Success(mapper.map(this.data))
        }
    }

}


fun <T> RequestState<T>?.takeSuccess(): T? = if (this is RequestState.Success) data else null
