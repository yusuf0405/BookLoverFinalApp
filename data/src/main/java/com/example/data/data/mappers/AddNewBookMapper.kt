package com.example.data.data.mappers

import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.models.BookThatReadPosterDb
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
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