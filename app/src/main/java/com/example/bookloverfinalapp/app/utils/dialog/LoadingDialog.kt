package com.example.bookloverfinalapp.app.utils.dialog

import android.app.ProgressDialog
import android.content.Context

@Suppress("DEPRECATION")
class LoadingDialog(private val context: Context, private val message: String) {

    private val dialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) { dialog() }

    private fun dialog(): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        return progressDialog
    }

    fun show() = dialog.show()

    fun dismiss() = dialog.dismiss()

}

