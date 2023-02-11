package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.adapter

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item

data class DayOfTheWeekAdapterModel(
    val day: String,
    val progress: Int
) : Item