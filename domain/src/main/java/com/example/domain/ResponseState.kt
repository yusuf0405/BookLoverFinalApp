package com.example.domain

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY;

    class Builder {

        private var actionSuccess: (() -> Unit)? = null
        private var actionError: (() -> Unit)? = null
        private var actionLoading: (() -> Unit)? = null
        private var actionEmpty: (() -> Unit)? = null

        fun handleSate(status: Status) = apply {
            when (status) {
                SUCCESS -> actionSuccess?.invoke()
                ERROR -> actionError?.invoke()
                LOADING -> actionLoading?.invoke()
                EMPTY -> actionEmpty?.invoke()
            }
        }

        fun handleSuccess(action: (() -> Unit)? = null) = apply {
            this@Builder.actionSuccess = action
        }

        fun handleError(action: (() -> Unit)? = null) = apply {
            this@Builder.actionError = action
        }

        fun handleLoading(action: (() -> Unit)? = null) = apply {
            this@Builder.actionLoading = action
        }

        fun handleEmpty(action: (() -> Unit)? = null) = apply {
            this@Builder.actionEmpty = action
        }
    }
}

data class Resource<T>(
    val status: Status,
    var data: T?,
    val message: String?,
    val exception: Exception?,
) {
    companion object {

        private var actionSuccess: (() -> Unit)? = null
        private var actionError: (() -> Unit)? = null
        private var actionLoading: (() -> Unit)? = null
        private var actionEmpty: (() -> Unit)? = null

        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null, exception = null)

        fun <T> error(message: String?, exception: Exception? = null): Resource<T> =
            Resource(status = Status.ERROR, data = null, message = message, exception = exception)

        fun <T> empty(): Resource<T> =
            Resource(status = Status.EMPTY, data = null, exception = null, message = null)

        fun <T> loading(): Resource<T> =
            Resource(status = Status.LOADING, data = null, message = null, exception = null)

        fun handleSate(status: Status) = apply {
            when (status) {
                Status.SUCCESS -> actionSuccess?.invoke()
                Status.ERROR -> actionError?.invoke()
                Status.LOADING -> actionLoading?.invoke()
                Status.EMPTY -> actionEmpty?.invoke()
            }
        }

        fun handleSuccess(action: (() -> Unit)? = null) = apply {
            this.actionSuccess = action
        }

        fun handleError(action: (() -> Unit)? = null) = apply {
            this.actionError = action
        }

        fun handleLoading(action: (() -> Unit)? = null) = apply {
            this.actionLoading = action
        }

        fun handleEmpty(action: (() -> Unit)? = null) = apply {
            this.actionEmpty = action
        }
    }
}

