package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.AudioBookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.ExclusiveAudioBookItem
import javax.inject.Inject

interface AudioBookToExclusiveAudioBookMapper {

    operator fun invoke(
        audioBook: AudioBook,
        audioBookItemOnClickListener: AudioBookItemOnClickListener
    ): ExclusiveAudioBookItem
}

class AudioBookToExclusiveAudioBookMapperImpl @Inject constructor(

) : AudioBookToExclusiveAudioBookMapper {
    override fun invoke(
        audioBook: AudioBook,
        audioBookItemOnClickListener: AudioBookItemOnClickListener
    ): ExclusiveAudioBookItem = audioBook.run {
        ExclusiveAudioBookItem(
            id = id,
            posterUrl = audioBookPoster.url,
            title = title,
            description = description,
            listener = audioBookItemOnClickListener
        )
    }
}