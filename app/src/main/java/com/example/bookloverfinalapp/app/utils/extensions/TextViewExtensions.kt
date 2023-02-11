package com.example.bookloverfinalapp.app.utils.extensions

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

fun TextView.makeTicker() {
    ellipsize = TextUtils.TruncateAt.MARQUEE
    isSelected = true
    isSingleLine = true
    marqueeRepeatLimit = -1
}

/**
 * Функция получает текст каждой строки в [TextView] и возращает строки под типом [ArrayList].
 */
fun TextView.getTextOfEachLine(): List<CharSequence> {
    val lines: MutableList<CharSequence> = ArrayList()
    val count: Int = lineCount
    for (line in 0 until count) {
        val start: Int = layout.getLineStart(line)
        val end: Int = layout.getLineEnd(line)
        val substring: CharSequence = text.subSequence(start, end)
        lines.add(substring)
    }
    return lines
}

/**
 * @param n -> количество строк, с помощью которого получаем текст длиной до этого количество.
 * Функция получает текст каждой строки, а затем берет первые [n] строк, и возращает в формате [String].
 */
fun TextView.getTextUpToNLine(n: Int): String {
    val linesList = getTextOfEachLine()
    var textUpToLine = String()
    if (linesList.size < n) return this.text.toString()
    linesList.take(n).forEach { text -> textUpToLine += text }
    return textUpToLine
}

fun SpannableStringBuilder.appendClickable(
    text: String,
    color: Int,
    action: () -> Unit
) {
    append(" ")
    append(text)
    setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {

            action()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }, indexOf(text), length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    setSpan(
        ForegroundColorSpan(color),
        indexOf(text),
        length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}