package com.example.bookloverfinalapp.app.models

import com.example.bookloverfinalapp.app.base.Abstract
import com.example.bookloverfinalapp.app.models.StudentBookAdapterModel.StudentStringMapper
import java.util.*

open class StudentBookAdapterModel :

    Abstract.Object<Unit, StudentStringMapper>() {

    override fun map(mapper: StudentStringMapper) {}


    object Progress : StudentBookAdapterModel()

    object Empty : StudentBookAdapterModel()


    class Fail(val message: String) : StudentBookAdapterModel() {
        override fun map(mapper: StudentStringMapper) {
            mapper.map(message = message)
        }
    }

    class Base(
        var author: String,
        var createdAt: Date,
        var bookId: String,
        var objectId: String,
        var page: Int,
        var publicYear: String,
        var title: String,
        var chapterCount: Int,
        var chaptersRead: Int,
        var poster: StudentBookPoster,
        var updatedAt: String,
        var book: StudentBookPdf,
        var progress: Int,
        private var isReadingPages: List<Boolean>,
    ) : StudentBookAdapterModel() {
        override fun map(mapper: StudentStringMapper) {
            mapper.map(
                author = author,
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

                )
        }
    }


    interface StudentStringMapper : Abstract.Mapper {

        fun map(message: String)
        fun map(
            author: String,
            createdAt: Date,
            bookId: String,
            objectId: String,
            page: Int,
            publicYear: String,
            title: String,
            chapterCount: Int,
            chaptersRead: Int,
            poster: StudentBookPoster,
            updatedAt: String,
            book: StudentBookPdf,
            progress: Int,
            isReadingPages: List<Boolean>,
        )
    }
}