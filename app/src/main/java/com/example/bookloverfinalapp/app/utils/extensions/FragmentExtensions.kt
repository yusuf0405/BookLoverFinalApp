package com.example.bookloverfinalapp.app.utils.extensions

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bookloverfinalapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun <T : Parcelable> Fragment.runWithArgumentsOrSkip(id: String, block: (arguments: T) -> Unit) =
    this.arguments?.getParcelable<T>(id)?.let(block) ?: Unit

fun Fragment.runWithArgumentsOrSkip(block: (bundle: Bundle) -> Unit) =
    this.arguments?.let(block) ?: Unit

/**
 * Показывает диалог асинхронно, т.е. диалог покажется не в момент вызова.
 * Если нужно показывать диалог в момент вызова, для избежания дублирования диалогов,
 * то использовать showNowOnlyOne(fragmentManager)
 */
fun DialogFragment.showOnlyOne(fragmentManager: FragmentManager) {
    if (fragmentManager.findFragmentByTag(this.javaClass.name) == null)
        this.show(fragmentManager, this.javaClass.name)
}

fun Fragment.createConfirmationDialog(
    @StringRes title: Int? = null,
    @StringRes message: Int? = null,
    @StringRes positiveButtonMessage: Int,
    @StringRes negativeButtonMessage: Int,
    @DrawableRes iconId: Int,
    isCancelable: Boolean = true,
    setOnPositiveButtonClickListener: () -> Unit,
    setOnNegativeButtonClickListener: () -> Unit = {}
): AlertDialog {
    val listener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> setOnPositiveButtonClickListener()
            DialogInterface.BUTTON_NEGATIVE -> setOnNegativeButtonClickListener()
        }
    }
    return MaterialAlertDialogBuilder(requireContext())
        .setTitle(title ?: R.string.empty)
        .setCancelable(isCancelable)
//        .setMessage(message ?: R.string.empty)
        .setIcon(iconId)
        .setPositiveButton(positiveButtonMessage, listener)
        .setNegativeButton(negativeButtonMessage, listener)
        .create()

}