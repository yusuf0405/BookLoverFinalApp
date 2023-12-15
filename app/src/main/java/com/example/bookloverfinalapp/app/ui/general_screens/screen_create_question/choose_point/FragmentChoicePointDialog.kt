package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_point

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentChoicePointDialogBinding
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage


class FragmentChoicePointDialog :
    BaseBindingFragment<FragmentChoicePointDialogBinding>(FragmentChoicePointDialogBinding::inflate) {

    private var setPointChoiceListener: ((Int) -> Unit)? = null
    private var checkedPoint: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setListeners()
    }

    private fun setupViews() = with(binding()) {
        val point = arguments?.getInt(POINT_KEY) ?: 0
        setupNumberPicker(numberPicker, point)
    }

    private fun setupNumberPicker(numberPicker: NumberPicker, point: Int) = numberPicker.apply {
        wrapSelectorWheel = true
        minValue = 0
        maxValue = 100
        value = point
    }

    private fun setListeners() = with(binding()) {
        numberPicker.setOnValueChangedListener { _, _, newVal -> checkedPoint = newVal }
        confirmButton.setOnDownEffectClickListener {
            setPointChoiceListener?.invoke(checkedPoint)
            dismissModalPage()
        }
        cancelButton.setOnDownEffectClickListener {
            dismissModalPage()
        }
    }

    companion object {
        private const val POINT_KEY = "POINT_KEY"

        fun newInstance(
            point: Int,
            setPointChoiceListener: (Int) -> Unit
        ) = FragmentChoicePointDialog().run {
            this.setPointChoiceListener = setPointChoiceListener
            arguments = bundleOf(POINT_KEY to point)
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}