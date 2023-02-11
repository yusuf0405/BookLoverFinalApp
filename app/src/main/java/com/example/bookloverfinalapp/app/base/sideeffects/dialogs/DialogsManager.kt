package com.example.bookloverfinalapp.app.base.sideeffects.dialogs

import com.example.data.cache.models.IdResourceString

interface DialogsManager {

    fun showConfigDialog(
        title: IdResourceString,
        message: IdResourceString,
        isCancelable: Boolean = true,
        positiveButtonMessage: IdResourceString,
        negativeButtonMessage: IdResourceString,
        setOnPositiveButtonClickListener: () -> Unit,
        setOnNegativeButtonClickListener: () -> Unit = {}
    )
}