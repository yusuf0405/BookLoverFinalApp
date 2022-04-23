package com.example.data.data.mappers

import com.example.data.data.models.StudentBookData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.domain.models.StudentBookPdfDomain
import com.example.domain.domain.models.StudentBookPosterDomain

class StudentBookDataMapper : Mapper<StudentBookData, StudentBookDomain>() {
    override fun map(from: StudentBookData): StudentBookDomain = from.run {
        StudentBookDomain(
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
            poster = StudentBookPosterDomain(name = poster.name, url = poster.url),
            book = StudentBookPdfDomain(name = book.name, url = book.url),
            bookId = bookId,
        )
    }
}