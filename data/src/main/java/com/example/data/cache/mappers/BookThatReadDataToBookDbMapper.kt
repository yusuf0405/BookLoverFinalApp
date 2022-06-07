package com.example.data.cache.mappers

import com.example.data.cache.models.BookThatReadCache
import com.example.data.cache.models.BookThatReadPosterCache
import com.example.data.models.BookThatReadData
import com.example.domain.Mapper

class BookThatReadDataToBookDbMapper : Mapper<BookThatReadData, BookThatReadCache> {

    override fun map(from: BookThatReadData): BookThatReadCache = from.run {
        BookThatReadCache(
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
            poster = BookThatReadPosterCache(name = poster.name, url = poster.url),
            book = book,
            bookId = bookId,
        )
    }
}