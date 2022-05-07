package com.example.domain.models

typealias Mapper<Input, Output> = (Input) -> Output

enum class Status {
    SUCCESS,
    ERROR,
    NETWORK_ERROR,
    LOADING,
    EMPTY,
}

data class Resource<T>(
    val status: Status,
    var data: T?,
    val message: String?,
    val exception: Exception?,
) {
    companion object {

        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null, exception = null)

        fun <T> error(message: String?): Resource<T> =
            Resource(status = Status.ERROR, data = null, message = message, exception = null)

        fun <T> empty(): Resource<T> =
            Resource(status = Status.EMPTY, data = null, exception = null, message = null)

        fun <T> networkError(): Resource<T> =
            Resource(status = Status.NETWORK_ERROR, data = null, message = null, exception = null)

        fun <T> loading(): Resource<T> =
            Resource(status = Status.LOADING, data = null, message = null, exception = null)
    }

    fun <R> map(mapper: Mapper<T, R>? = null): Resource<R> = when (this.status) {
        Status.ERROR -> error(this.message!!)
        Status.EMPTY -> empty()
        Status.LOADING -> loading()
        Status.NETWORK_ERROR -> networkError()
        Status.SUCCESS -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be NULL for success result")
            success(mapper(this.data!!))
        }
    }
}

