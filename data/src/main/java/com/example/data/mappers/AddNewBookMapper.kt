package com.example.data.mappers

import com.example.data.cache.models.BookThatReadCache
import com.example.data.cache.models.BookThatReadPosterCache
import com.example.data.cloud.models.AddNewBookThatReadCloud
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookThatReadDomain
import java.util.*

class AddNewBookMapper : Mapper<AddNewBookThatReadDomain, AddNewBookThatReadCloud> {
    override fun map(from: AddNewBookThatReadDomain): AddNewBookThatReadCloud = from.run {
        AddNewBookThatReadCloud(
            progress = progress,
            bookId = bookId,
            userId = userId,
            isReadingPages, chaptersRead,
            path = book
        )
    }
}

internal fun AddNewBookThatReadDomain.toStudentBook(
    objectId: String,
    createdAt: Date,
    path: String,
): BookThatReadCache =
    BookThatReadCache(
        objectId = objectId,
        createdAt = createdAt,
        chaptersRead = chaptersRead,
        author = author,
        bookId = bookId,
        progress = progress,
        page = page,
        publicYear = publicYear,
        title = title,
        chapterCount = chapterCount,
        poster = BookThatReadPosterCache(name = poster.name, url = poster.url),
        book = path,
        updatedAt = createdAt,
        isReadingPages = isReadingPages
    )