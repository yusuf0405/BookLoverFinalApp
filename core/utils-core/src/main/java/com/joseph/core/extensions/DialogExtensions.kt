package com.joseph.core.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior

private const val DIALOG_MARGIN_DP = 0

/**
 * Показывает диалог асинхронно, т.е. диалог покажется не в момент вызова.
 * Если нужно показывать диалог в момент вызова, для избежания дублирования диалогов,
 * то использовать showNowOnlyOne(fragmentManager)
 */
fun DialogFragment.showOnlyOne(fragmentManager: FragmentManager) {
    if (this.isAdded) return
    if (fragmentManager.findFragmentByTag(this.javaClass.name) == null)
        this.show(fragmentManager, this.javaClass.name)
}

fun DialogFragment?.tuneBottomDialog(
    marginBottom: Int = 0,
) {
    this?.dialog?.let { dialog ->
        val window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowLayoutParams = window.attributes
        windowLayoutParams.gravity = Gravity.BOTTOM
        windowLayoutParams.width = requireActivity().getWidth() - dpToPx(
            requireContext(),
            2 * DIALOG_MARGIN_DP
        )
        windowLayoutParams.y = dpToPx(requireContext(), marginBottom)
        window.attributes = windowLayoutParams
    }
}

fun DialogFragment?.tuneCenterDialog(
    marginHorizontal: Int = 16,
) {
    this?.dialog?.let { dialog ->
        val window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowLayoutParams = window.attributes
        windowLayoutParams.gravity = Gravity.CENTER
        windowLayoutParams.width = requireActivity().getWidth() - dpToPx(
            requireContext(),
            2 * marginHorizontal
        )
        window.attributes = windowLayoutParams
    }
}


fun dpToPx(context: Context, dp: Int): Int {
    return dp * getMetrics(context).densityDpi / 160
}

fun getMetrics(context: Context): DisplayMetrics {
    return context.resources.displayMetrics
}

/**
 * Extension for opening BottomSheetDialogFragment in to full height
 * WARNING! Call this method only in OnStart callback
 */
fun DialogFragment.openInFullHeightIfCan() {
    view?.let { view ->
        view.post {
            view.parent?.let { parent ->
                if (parent is View) {
                    val params = parent.layoutParams
                    if (params is CoordinatorLayout.LayoutParams && params.behavior is BottomSheetBehavior) {
                        (params.behavior as BottomSheetBehavior).peekHeight = view.measuredHeight
                        (params.behavior as BottomSheetBehavior).state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
    }
}

fun FragmentActivity.getWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}


fun DialogFragment?.tuneLyricsDialog() {
    this?.dialog?.let { dialog ->
        val window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}