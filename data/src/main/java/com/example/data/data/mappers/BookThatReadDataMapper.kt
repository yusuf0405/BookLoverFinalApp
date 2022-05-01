package com.example.data.data.mappers

import com.example.data.data.models.BookThatReadData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.models.BookThatReadPosterDomain

class BookThatReadDataMapper : Mapper<BookThatReadData, BookThatReadDomain>() {
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