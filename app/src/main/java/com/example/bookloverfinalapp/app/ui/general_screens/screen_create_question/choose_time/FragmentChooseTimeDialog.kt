package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_time

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentChoosePointBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage


class FragmentChooseTimeDialog :
    BaseBindingFragment<FragmentChoosePointBinding>(FragmentChoosePointBinding::inflate) {

    private var setTimeChoiceListener: ((minute: Int, seconds: Int) -> Unit)? = null

    private var checkedMinutes: Int = 0
    private var checkedSeconds: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setListeners()
    }

    private fun setupViews() = with(binding()) {
        val minutes = arguments?.getInt(MINUTES_KEY) ?: 0
        val seconds = arguments?.getInt(SECONDS_KEY) ?: 0
        timePicker.setIs24HourView(true)
        timePicker.currentHour = minutes
        timePicker.currentMinute = seconds
    }

    private fun setListeners() = with(binding()) {
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            checkedMinutes = hourOfDay
            checkedSeconds = minute
        }
        confirmButton.setOnDownEffectClickListener {
            setTimeChoiceListener?.invoke(checkedMinutes, checkedSeconds)
            dismissModalPage()
        }
        cancelButton.setOnDownEffectClickListener { dismissModalPage() }
    }

    companion object {
        private const val MINUTES_KEY = "MINUTES_KEY"
        private const val SECONDS_KEY = "SECONDS_KEY"
        fun newInstance(
            checkedMinutes: Int,
            checkedSeconds: Int,
            setTimeChoiceListener: (minute: Int, seconds: Int) -> Unit
        ) = FragmentChooseTimeDialog().run {
            arguments = bundleOf(
                MINUTES_KEY to checkedMinutes,
                SECONDS_KEY to checkedSeconds
            )
            this.setTimeChoiceListener = setTimeChoiceListener
            ModalPage.Builder()
                .fragment(this)
                .build()
        }

    }
}