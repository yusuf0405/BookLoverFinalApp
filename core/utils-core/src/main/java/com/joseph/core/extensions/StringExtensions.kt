package com.joseph.core.extensions

import android.text.SpannableStringBuilder

fun String.replaceCharsByCondition(
    with: Char,
    startIndexInclusive: Int,
    endIndexExclusive: Int,
    condition: (Char) -> Boolean
): String {
    val newStringBuilder = SpannableStringBuilder()
    forEachIndexed { i, char ->
        if (condition(char)
            && i >= startIndexInclusive
            && i < endIndexExclusive
        ) {
            newStringBuilder.append(with)
        } else {
            newStringBuilder.append(char)
        }
    }
    return newStringBuilder.toString()
}