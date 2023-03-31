package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.ExclusiveBookItem
import javax.inject.Inject

interface BookToExclusiveBookMapper {

    operator fun invoke(
        book: Book,
        bookItemOnClickListener: BookItemOnClickListener
    ): ExclusiveBookItem
}

class BookToExclusiveBookMapperImpl @Inject constructor() : BookToExclusiveBookMapper {

    override fun invoke(
        book: Book,
        bookItemOnClickListener: BookItemOnClickListener
    ): ExclusiveBookItem = book.run {
        ExclusiveBookItem(
            id = id,
            posterUrl = poster.url,
            description = description,
            title = title,
            listener = bookItemOnClickListener,
        )
    }
}