package com.example.data.mappers

import com.example.data.models.BookData
import com.example.domain.Mapper
import com.example.domain.models.BookDomain
import com.example.domain.models.BookPdfDomain
import com.example.domain.models.BookPosterDomain

class BookDataToDomainMapper : Mapper<BookData, BookDomain>() {
    override fun map(from: BookData): BookDomain = from.run {
        BookDomain(author = author,
            createdAt = createdAt,
            page = page,
            book = BookPdfDomain(
                name = book.name,
                type = book.type,
                url = book.url
            ),
            title = title,
            chapterCount = chapterCount,
            poster = BookPosterDomain(
                name = poster.name,
                url = poster.url
            ),
            updatedAt = updatedAt, id = id,
            publicYear = publicYear,
            genres = genres)
    }
}