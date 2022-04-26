package com.example.data.data.cache.mappers

import com.example.domain.domain.Mapper
import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cache.models.StudentBookPdfDb
import com.example.data.data.cache.models.StudentBookPosterDb
import com.example.data.data.models.StudentBookData

class StudentBookDataToBookDbMapper : Mapper<StudentBookData, StudentBookDb>() {

    override fun map(from: StudentBookData): StudentBookDb = from.run {
        StudentBookDb(
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
            poster = StudentBookPosterDb(name = poster.name, url = poster.url),
            book = StudentBookPdfDb(name = book.name, url = book.url),
            bookId = bookId,
        )
    }
}