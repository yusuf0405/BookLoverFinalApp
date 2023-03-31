package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookGenreItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.BookGenreItem
import com.example.domain.models.GenreDomain
import javax.inject.Inject

interface GenreMapBookGenreItemMapper {

    operator fun invoke(
        genre: GenreDomain,
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners
    ): BookGenreItem
}

class GenreMapBookGenreItemMapperImpl @Inject constructor() : GenreMapBookGenreItemMapper {

    override fun invoke(
        genre: GenreDomain,
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners
    ) = genre.run {
        BookGenreItem(
            id = genre.id,
            titles = genre.titles,
            posterUrl = genre.poster.url,
            listener = bookGenreItemOnClickListeners
        )
    }
}