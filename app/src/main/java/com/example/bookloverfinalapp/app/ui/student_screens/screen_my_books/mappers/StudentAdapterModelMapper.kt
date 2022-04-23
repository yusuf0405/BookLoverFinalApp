package com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.mappers

import com.example.domain.domain.Mapper
import com.example.domain.domain.models.StudentBookDomain
import com.example.bookloverfinalapp.app.models.StudentBookAdapterModel
import com.example.bookloverfinalapp.app.models.StudentBookPdf
import com.example.bookloverfinalapp.app.models.StudentBookPoster

class StudentAdapterModelMapper : Mapper<StudentBookDomain, StudentBookAdapterModel.Base>() {

    override fun map(from: StudentBookDomain): StudentBookAdapterModel.Base = from.run {
        StudentBookAdapterModel.Base(
            createdAt = createdAt,
            bookId = bookId,
            page = page,
            publicYear = publicYear,
            book = StudentBookPdf(url = book.url, name = book.name),
            title = title,
            chapterCount = chapterCount,
            poster = StudentBookPoster(url = poster.url, name = poster.name),
            updatedAt = updatedAt,
            objectId = objectId,
            isReadingPages = isReadingPages,
            chaptersRead = chaptersRead,
            progress = progress,
            author = author
        )
    }
}