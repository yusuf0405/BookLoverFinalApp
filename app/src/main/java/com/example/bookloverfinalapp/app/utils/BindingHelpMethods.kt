package com.example.bookloverfinalapp.app.utils

/** Throws an [IllegalStateException] with binding lifecycle error message. */
inline fun bindingLifecycleError(): Nothing = throw IllegalStateException(
    "This property is only valid between onCreateView and onDestroyView."
)