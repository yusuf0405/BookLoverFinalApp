package com.joseph.ui_core.custom.snackbar

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.joseph.ui_core.extensions.toDp
import com.joseph.ui_core.extensions.setBackgroundColorToTransperent
import com.joseph.ui_core.extensions.setPaddingsZero

interface GenericSnackbarInflter {

    fun inflate(rootView: View, customLayout: View, duration: Int): Snackbar

    class Base : GenericSnackbarInflter {
        private companion object {
            val DEFAULT_MARGIN_SIZE = 12.toDp
        }

        override fun inflate(rootView: View, customLayout: View, duration: Int): Snackbar {
            val snackbar = Snackbar.make(rootView, String(), duration)
            with(snackbar.view) {
                setPaddingsZero()
                setBackgroundColorToTransperent(context)

                val metrics = DisplayMetrics()

                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val display = context.display
                    display?.getRealMetrics(metrics)
                } else {
                    val display =
                        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                    display.getMetrics(metrics)
                }

                val width = metrics.widthPixels
                var bottomMargin = 18.toDp

                for (i in 0 until (rootView as ViewGroup).childCount) {
                    val view = rootView.getChildAt(i)
                    if (view is BottomNavigationView) {
                        bottomMargin = 68.toDp
                        break
                    }
                }

                val snackbarLayoutParams =
                    FrameLayout.LayoutParams(width - 24.toDp, ViewGroup.LayoutParams.WRAP_CONTENT)
                customLayout.layoutParams = snackbarLayoutParams
                (customLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    DEFAULT_MARGIN_SIZE,
                    0,
                    DEFAULT_MARGIN_SIZE,
                    snackbarLayoutParams.bottomMargin + bottomMargin,
                )
                (customLayout.layoutParams as FrameLayout.LayoutParams)
                    .gravity = Gravity.CENTER_HORIZONTAL
                (this as ViewGroup).removeAllViews()
                addView(customLayout)
            }
            return snackbar
        }
    }
}