package com.example.data.data.cloud.mappers

import com.example.domain.domain.Mapper
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.models.BookData
import com.example.data.data.models.BookPdfData
import com.example.data.data.models.BookPosterData

class BookCloudDataMapper : Mapper<BookCloud, BookData>() {

    override fun map(from: BookCloud): BookData = from.run {
        BookData(
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
    }
}