package com.example.bookloverfinalapp.app.base.sideeffects.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.ActivityMain
import com.example.data.cache.models.IdResourceString
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class DialogsManagerImpl @Inject constructor(
    private val activity: ActivityMain
) : DialogsManager {

    override fun showConfigDialog(
        title: IdResourceString,
        message: IdResourceString,
        isCancelable: Boolean,
        positiveButtonMessage: IdResourceString,
        negativeButtonMessage: IdResourceString,
        setOnPositiveButtonClickListener: () -> Unit,
        setOnNegativeButtonClickListener: () -> Unit
    ) {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    setOnPositiveButtonClickListener()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    setOnNegativeButtonClickListener()
                }
            }
        }
        val dialog = AlertDialog.Builder(activity)
            .setTitle(title.id)
            .setCancelable(isCancelable)
            .setMessage(message.id)
            .setPositiveButton(positiveButtonMessage.id, listener)
            .setNegativeButton(negativeButtonMessage.id, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()
        dialog.show()
    }
}