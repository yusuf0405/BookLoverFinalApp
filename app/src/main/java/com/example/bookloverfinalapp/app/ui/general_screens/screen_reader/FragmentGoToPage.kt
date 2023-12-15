package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentGoToPageBinding
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage

class FragmentGoToPage :
    BaseBindingFragment<FragmentGoToPageBinding>(FragmentGoToPageBinding::inflate) {

    private var listener: ((Int) -> Unit?)? = null
    private var currentPage = 0
    private var descriptionText = String()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        setupViews()
    }

    private fun setupViews() = with(binding()) {
        goPageEditText.setText(currentPage.toString())
        description.text = descriptionText
        goPageEditText.requestFocus()
        confirmButton.setOnDownEffectClickListener { handleConfirmButtonClick() }
        cancelButton.setOnDownEffectClickListener { dismissModalPage() }
    }

    private fun handleConfirmButtonClick() = with(binding()) {
        val enteredText = goPageEditText.text.toString().trim()
        if (enteredText.isBlank()) {
            goPageEditText.error = getString(R.string.empty_value)
            return@with
        }
        val newPage = enteredText.toInt()
        listener?.invoke(newPage)
        dismissModalPage()
    }

    companion object {
        fun newInstance(title: String, currentPage: Int, listener: (Int) -> Unit) =
            FragmentGoToPage().run {
                this.listener = listener
                this.currentPage = currentPage
                this.descriptionText = title
                ModalPage.Builder()
                    .fragment(this)
                    .build()
            }
    }
}