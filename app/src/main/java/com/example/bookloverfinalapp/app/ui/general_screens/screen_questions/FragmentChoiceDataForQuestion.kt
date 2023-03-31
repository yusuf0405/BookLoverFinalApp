package com.example.bookloverfinalapp.app.ui.general_screens.screen_questions

import android.os.Bundle
import android.view.View
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.databinding.FragmentChoiceDataForQuestionBinding
import java.text.DateFormat
import java.util.*

class FragmentChoiceDataForQuestion :
    BaseBindingFragment<FragmentChoiceDataForQuestionBinding>(FragmentChoiceDataForQuestionBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val selectedDate = calendarView.date
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDate

            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
        }


    }

}