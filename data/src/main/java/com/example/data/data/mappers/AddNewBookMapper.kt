package com.example.data.data.mappers

import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cache.models.StudentBookPdfDb
import com.example.data.data.cache.models.StudentBookPosterDb
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
            isReadingPages, chaptersRead
        )
    }
}

internal fun AddNewBookDomain.toStudentBook(objectId: String, createdAt: Date): StudentBookDb =
    StudentBookDb(
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
        poster = StudentBookPosterDb(name = poster.name, url = poster.url),
        book = StudentBookPdfDb(name = book.name, url = book.url),
        updatedAt = createdAt,
        isReadingPages = isReadingPages
    )