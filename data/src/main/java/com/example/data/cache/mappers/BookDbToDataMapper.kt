package com.example.data.cache.mappers

import com.example.data.cache.models.BookCache
import com.example.data.models.BookData
import com.example.data.models.BookPdfData
import com.example.data.models.BookPosterData
import com.example.domain.Mapper

class BookDbToDataMapper : Mapper<BookCache, BookData> {
    override fun map(from: BookCache): BookData = from.run {
        val bookData = BookData(
            genres = genres,
            author = author,
            createdAt = createdAt,
            page = page,
            book = BookPdfData(
                name = book.name,
                type = book.type,
                url = book.url
            ),
            title = title,
            chapterCount = chapterCount,
            poster = BookPosterData(
                name = poster.name,
                url = poster.url
            ),
            updatedAt = updatedAt, id = id,
            publicYear = publicYear
        )
        bookData
    }
}