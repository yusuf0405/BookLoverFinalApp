package com.example.bookloverfinalapp.app.utils.extensions

fun <T> List<T>.swapElements(): List<T> {
    val list = mutableListOf<T>()
    for ((counter, i) in (size - 1 downTo 0).withIndex()) {
        list.add(counter, this[i])
    }
    return list
}