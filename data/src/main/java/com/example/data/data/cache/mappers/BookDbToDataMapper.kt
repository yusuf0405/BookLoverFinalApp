package com.example.data.data.cache.mappers

import com.example.domain.domain.Mapper
import com.example.data.data.cache.models.BookDb
import com.example.data.data.models.BookData
import com.example.data.data.models.BookPdfData
import com.example.data.data.models.BookPosterData

class BookDbToDataMapper : Mapper<BookDb, BookData>() {
    override fun map(from: BookDb): BookData = from.run {
        val bookData = BookData(
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