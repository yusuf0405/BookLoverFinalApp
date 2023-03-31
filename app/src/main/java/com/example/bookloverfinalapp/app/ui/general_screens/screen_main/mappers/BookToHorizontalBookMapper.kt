package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener
import javax.inject.Inject

interface BookToHorizontalBookMapper {

    operator fun invoke(
        book: Book,
        bookItemOnClickListener: BookItemOnClickListener
    ): HorizontalBookAdapterModel
}

class BookToHorizontalBookMapperImpl @Inject constructor() : BookToHorizontalBookMapper {
    override fun invoke(
        book: Book,
        bookItemOnClickListener: BookItemOnClickListener
    ): HorizontalBookAdapterModel = book.run {
        HorizontalBookAdapterModel(
            id = id,
            posterUrl = poster.url,
            author = author,
            description = description,
            savedStatus = savedStatus,
            title = title,
            listener = bookItemOnClickListener
        )
    }
}