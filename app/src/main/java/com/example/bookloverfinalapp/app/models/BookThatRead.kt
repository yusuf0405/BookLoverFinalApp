package com.example.bookloverfinalapp.app.models

import org.ocpsoft.prettytime.PrettyTime
import java.io.Serializable
import java.util.*

data class BookThatRead(
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
    var isReadingPages: List<Boolean>,
) : Serializable {

    fun getCreatedAt(): String {
        val prettyTime = PrettyTime(Locale("ru"))
        return prettyTime.format(createdAt)
    }
}

data class BookThatReadPoster(
    var name: String,
    var url: String,
)
