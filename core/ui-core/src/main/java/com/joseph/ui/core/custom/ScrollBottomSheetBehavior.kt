package com.joseph.ui.core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ScrollBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {

    var isAllowDragging = true

    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        return isAllowDragging && super.onInterceptTouchEvent(parent, child, event)
    }
}