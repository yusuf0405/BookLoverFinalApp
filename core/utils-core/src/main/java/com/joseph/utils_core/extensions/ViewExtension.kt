package com.joseph.utils_core.extensions

import android.graphics.Rect
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.thekhaeng.pushdownanim.PushDownAnim

fun View.getPixelFromDp(paddingDp: Int): Float {
    val density = context.resources.displayMetrics.density
    return (paddingDp * density)
}

fun View.getDpFromPixel(paddingDp: Int): Float {
    val density = context.resources.displayMetrics.density
    return (paddingDp / density)
}

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.GONE
}

fun View.setPaddingTopHeightStatusBar(){
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        v.updatePadding(top = insets.systemWindowInsetTop)
        insets.consumeSystemWindowInsets()
    }
}
fun View.setOnApplyStatusBarInsetsListener(listener: (Insets) -> Unit){
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        listener(insets.toStatusBarInsets())
        insets
    }
}
fun WindowInsetsCompat.toStatusBarInsets() = getInsets(
    WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout()
)

fun View.downEffect(): View {
    PushDownAnim.setPushDownAnimTo(this)
    return this
}

fun View.setOnDownEffectClickListener(onClickListener: View.OnClickListener): View {
    PushDownAnim.setPushDownAnimTo(this).setOnClickListener(onClickListener)
    return this
}

fun View.pointInView(pointX: Float, pointY: Float): Boolean {
    val outRect = Rect()
    val location = IntArray(2)

    getDrawingRect(outRect)
    getLocationOnScreen(location)
    outRect.offset(location[0], location[1])

    return outRect.contains(pointX.toInt(), pointY.toInt())
}