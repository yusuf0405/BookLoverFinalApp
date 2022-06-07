package com.example.data.mappers

import com.example.data.models.BookThatReadData
import com.example.domain.Mapper
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.BookThatReadPosterDomain

class BookThatReadDataMapper : Mapper<BookThatReadData, BookThatReadDomain> {
    override fun map(from: BookThatReadData): BookThatReadDomain = from.run {
        BookThatReadDomain(
            objectId = objectId,
            title = title,
            author = author,
            updatedAt = updatedAt,
            page = page,
            createdAt = createdAt,
            chapterCount = chapterCount,
            publicYear = publicYear,
            chaptersRead = chaptersRead,
            progress = progress,
            isReadingPages = isReadingPages,
            poster = BookThatReadPosterDomain(name = poster.name, url = poster.url),
            book = book,
            bookId = bookId,
        )
    }
}