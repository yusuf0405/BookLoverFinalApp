package com.example.data.mappers

import com.example.data.cache.models.BookThatReadDb
import com.example.data.cache.models.BookThatReadPosterDb
import com.example.data.cloud.models.AddNewBookCloud
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookDomain
import java.util.*

class AddNewBookMapper : Mapper<AddNewBookDomain, AddNewBookCloud>() {
    override fun map(from: AddNewBookDomain): AddNewBookCloud = from.run {
        AddNewBookCloud(
            progress = progress,
            bookId = bookId,
            userId = userId,
            isReadingPages, chaptersRead,
            path = book
        )
    }
}

internal fun AddNewBookDomain.toStudentBook(
    objectId: String,
    createdAt: Date,
    path: String,
): BookThatReadDb =
    BookThatReadDb(
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
        poster = BookThatReadPosterDb(name = poster.name, url = poster.url),
        book = path,
        updatedAt = createdAt,
        isReadingPages = isReadingPages
    )