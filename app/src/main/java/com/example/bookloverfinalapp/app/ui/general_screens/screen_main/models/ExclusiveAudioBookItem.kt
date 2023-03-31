package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.AudioBookItemOnClickListener

data class ExclusiveAudioBookItem(
    val id: String,
    val posterUrl: String,
    val title: String,
    val description: String,
    val listener: AudioBookItemOnClickListener
) : Item