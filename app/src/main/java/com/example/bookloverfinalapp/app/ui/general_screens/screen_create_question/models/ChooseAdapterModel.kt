package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.models

import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.listener.ChooseModelOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.select_value_option.Value
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item

data class ChooseAdapterModel(
    val title: String,
    val value: Int,
    var isChecked: Boolean,
    val listener: ChooseModelOnClickListeners
) : Item, Value {

    companion object {

    }
}