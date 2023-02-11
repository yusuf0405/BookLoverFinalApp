package com.example.bookloverfinalapp.app.utils.extensions

/**
 * Этот код удаляет последние n символов из строки.
 */
fun String?.removeLastNChars(n: Int): String =
    if (this.isNullOrEmpty()) String()
    else if (this.length < n) this
    else this.substring(0, this.length - n)