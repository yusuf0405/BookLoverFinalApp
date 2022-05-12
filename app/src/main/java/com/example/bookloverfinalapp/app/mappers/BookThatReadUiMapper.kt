package com.example.bookloverfinalapp.app.mappers

import com.example.domain.Mapper
import com.example.domain.models.BookThatReadDomain
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.BookThatReadPoster

class BookThatReadUiMapper : Mapper<BookThatReadDomain, BookThatRead>() {

    override fun map(from: BookThatReadDomain): BookThatRead = from.run {
        BookThatRead(createdAt = createdAt,
            bookId = bookId,
            page = page,
            publicYear = publicYear,
            book = book,
            title = title,
            chapterCount = chapterCount,
            poster = BookThatReadPoster(url = poster.url, name = poster.name),
            updatedAt = updatedAt,
            objectId = objectId,
            isReadingPages = isReadingPages,
            chaptersRead = chaptersRead,
            progress = progress,
            author = author)
    }
}