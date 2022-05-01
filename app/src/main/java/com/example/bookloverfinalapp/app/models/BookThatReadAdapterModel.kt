package com.example.bookloverfinalapp.app.models

import com.example.bookloverfinalapp.app.base.Abstract
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel.BookThatReadStringMapper
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

open class BookThatReadAdapterModel :

    Abstract.Object<Unit, BookThatReadStringMapper>() {

    override fun map(mapper: BookThatReadStringMapper) {}


    object Progress : BookThatReadAdapterModel()

    object Empty : BookThatReadAdapterModel()


    class Fail(val message: String) : BookThatReadAdapterModel() {
        override fun map(mapper: BookThatReadStringMapper) {
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
        var poster: BookThatReadPoster,
        var updatedAt: Date,
        var book: String,
        var progress: Int,
        private var isReadingPages: List<Boolean>,
    ) : BookThatReadAdapterModel() {
        override fun map(mapper: BookThatReadStringMapper) {
            mapper.map(
                author = author,
                createdAt = createdAt,
                bookId = bookId,
                page = page,
                publicYear = publicYear,
                book = book,
                title = title,
                chapterCount = chapterCount,
                poster = BookThatReadPoster(url = poster.url, name = poster.name),
                updatedAt = updatedAt,
                objectId = objectId,
                isReadingPages = isReadingPages,
                chaptersRead = chaptersRead,
                progress = progress,

                )
        }

        fun getCreatedAt(): String {
            val prettyTime = PrettyTime(Locale("ru"))
            return prettyTime.format(createdAt)
        }
    }


    interface BookThatReadStringMapper : Abstract.Mapper {

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
            poster: BookThatReadPoster,
            updatedAt: Date,
            book: String,
            progress: Int,
            isReadingPages: List<Boolean>,
        )
    }
}