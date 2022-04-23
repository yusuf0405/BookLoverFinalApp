package com.example.data.data.cache.mappers

import com.example.data.data.cache.models.StudentBookDb
import com.example.domain.domain.Mapper
import com.example.data.data.models.StudentBookData
import com.example.data.data.models.StudentBookPdfData
import com.example.data.data.models.StudentBookPosterData

class StudentBookDbToDataMapper : Mapper<StudentBookDb, StudentBookData>() {

    override fun map(from: StudentBookDb): StudentBookData = from.run {
        StudentBookData(
            author = author,
            createdAt = createdAt,
            bookId = bookId,
            objectId = objectId,
            page = page,
            publicYear = publicYear,
            title = title,
            chapterCount = chapterCount,
            chaptersRead = chaptersRead,
            updatedAt = updatedAt,
            poster = StudentBookPosterData(name = poster.name, url = poster.url),
            book = StudentBookPdfData(name = book.name, url = book.url),
            isReadingPages = isReadingPages,
            progress = progress
        )
    }
}