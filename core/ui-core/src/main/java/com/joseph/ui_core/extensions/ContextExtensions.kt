package com.joseph.ui_core.extensions

import android.content.Context
import android.graphics.Canvas
import android.text.StaticLayout
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.core.graphics.withTranslation

fun Context.layoutInflater(): LayoutInflater = LayoutInflater.from(this)

/**
 * Context Extension для конвертирования значения в пиксели.
 * @property dp - значение density-independent pixels
 */
fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

/**
 * Context Extension для конвертирования значения размера шрифта в пиксели.
 * @property sp - значение scale-independent pixels
 */
fun Context.spToPx(sp: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), this.resources.displayMetrics);
}

fun Context.getStatusBarHeight(): Int =
    this.resources
        .getIdentifier("status_bar_height", "dimen", "android")
        .takeIf { resourceId -> resourceId > 0 }
        ?.let { resourceId -> this.resources.getDimensionPixelSize(resourceId) }
        ?: 0