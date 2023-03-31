package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.confim_dialog

import android.os.Bundle
import android.view.View
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentConfirmDialogBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage


class FragmentConfirmDialog :
    BaseBindingFragment<FragmentConfirmDialogBinding>(FragmentConfirmDialogBinding::inflate) {

    private var setOnPositiveButtonClickListener: (() -> Unit?)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        confim.setOnDownEffectClickListener {
            setOnPositiveButtonClickListener?.invoke()
            dismissModalPage()
        }
        cancel.setOnDownEffectClickListener {
            dismissModalPage()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            setOnPositiveButtonClickListener: () -> Unit,
        ) = FragmentConfirmDialog().run {
            this.setOnPositiveButtonClickListener = setOnPositiveButtonClickListener
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}