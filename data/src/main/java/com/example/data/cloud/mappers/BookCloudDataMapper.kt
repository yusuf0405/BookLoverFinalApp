package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookCloud
import com.example.data.models.BookData
import com.example.data.models.BookPdfData
import com.example.data.models.BookPosterData
import com.example.domain.Mapper

class BookCloudDataMapper : Mapper<BookCloud, BookData> {

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
            publicYear = publicYear,
            genres = genres
        )
    }
}