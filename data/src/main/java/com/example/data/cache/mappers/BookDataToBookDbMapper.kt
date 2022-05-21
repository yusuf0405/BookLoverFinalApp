package com.example.data.cache.mappers

import com.example.data.cache.models.BookDb
import com.example.data.cache.models.BookPdfDb
import com.example.data.cache.models.BookPosterDb
import com.example.data.models.BookData
import com.example.domain.Mapper

class BookDataToBookDbMapper : Mapper<BookData, BookDb>() {

    override fun map(from: BookData): BookDb = from.run {
        BookDb(
            id = id,
            title = title,
            author = author,
            updatedAt = updatedAt,
            page = page,
            createdAt = createdAt,
            chapterCount = chapterCount,
            publicYear = publicYear,
            poster = BookPosterDb(
                name = poster.name,
                url = poster.url
            ),
            book = BookPdfDb(
                name = book.name,
                url = book.url,
                type = book.type
            ),
            genres = genres
        )
    }
}