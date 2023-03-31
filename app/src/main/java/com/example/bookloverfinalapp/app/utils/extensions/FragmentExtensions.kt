package com.example.bookloverfinalapp.app.utils.extensions

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.service_player.BasePlayerActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun <T : Parcelable> Fragment.runWithArgumentsOrSkip(id: String, block: (arguments: T) -> Unit) =
    this.arguments?.getParcelable<T>(id)?.let(block) ?: Unit

fun Fragment.showPlayerOverlay() {
    val playerActivity = requireActivity() as? BasePlayerActivity? ?: return
    playerActivity.setPlayerStateToExpended()
}

fun Fragment.dismissPlayerOverlay() {
    val playerActivity = requireActivity() as? BasePlayerActivity? ?: return
    playerActivity.setPlayerStateToCollapsed()
}

fun Fragment.replace(
    target: Fragment,
    @IdRes containerId: Int = R.id.nav_host_fragment_activity_main,
    fragmentManager: FragmentManager = requireActivity().supportFragmentManager
) {
    val beginTransaction = fragmentManager.beginTransaction()
    beginTransaction.replace(containerId, target)
    beginTransaction.addToBackStack(null)
    beginTransaction.commit()
}

fun Fragment.runWithArgumentsOrSkip(block: (bundle: Bundle) -> Unit) =
    this.arguments?.let(block) ?: Unit



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