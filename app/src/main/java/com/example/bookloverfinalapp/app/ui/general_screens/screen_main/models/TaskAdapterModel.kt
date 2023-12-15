package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui.core.adapter.Item
import java.text.SimpleDateFormat
import java.util.*

data class TaskAdapterModel(
    val id: String,
    val title: String,
    val description: String,
    val classId: String,
    val startDate: Date,
    val endDate: Date,
    val taskGenres: List<String>
) : Item {

    fun getDate(): String {
        val formatter = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val start = formatter.format(startDate).toString()
        val end = formatter.format(endDate).toString()
        return "$start to $end"
    }
}