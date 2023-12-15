package com.joseph.theme

import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.animation.AccelerateDecelerateInterpolator

interface PushDown {
    fun setScale(scale: Float): PushDown?
    fun setScale(@PushDownAnim.Mode mode: Int, scale: Float): PushDown
    fun setDurationPush(duration: Long): PushDown
    fun setDurationRelease(duration: Long): PushDown
    fun setInterpolatorPush(interpolatorPush: AccelerateDecelerateInterpolator): PushDown
    fun setInterpolatorRelease(interpolatorRelease: AccelerateDecelerateInterpolator): PushDown
    fun setOnClickListener(clickListener: View.OnClickListener): PushDown
    fun setOnLongClickListener(clickListener: OnLongClickListener): PushDown
    fun setOnTouchEvent(eventListener: OnTouchListener?): PushDown
}
