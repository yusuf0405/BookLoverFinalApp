package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.AudioBookItemOnClickListener

data class AudioBookAdapterModel(
    val audioBook: AudioBook,
    val listener: AudioBookItemOnClickListener
) : Item {

    fun id() = audioBook.id + audioBook.title + audioBook.author
}