package com.joseph.ui.core.extensions

fun String.firstLetterCapital(): String {
    return if (this.isEmpty()) return this
    else substring(0, 1).uppercase() + substring(1).lowercase()
}