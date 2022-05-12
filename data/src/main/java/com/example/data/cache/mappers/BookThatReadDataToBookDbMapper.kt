package com.example.data.cache.mappers

import com.example.data.cache.models.BookThatReadDb
import com.example.data.cache.models.BookThatReadPosterDb
import com.example.data.models.BookThatReadData
import com.example.domain.Mapper

class BookThatReadDataToBookDbMapper : Mapper<BookThatReadData, BookThatReadDb>() {

    override fun map(from: BookThatReadData): BookThatReadDb = from.run {
        BookThatReadDb(
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
            poster = BookThatReadPosterDb(name = poster.name, url = poster.url),
            book = book,
            bookId = bookId,
        )
    }
}