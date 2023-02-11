package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@Parcelize
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
    var bookPdfUrl: String,
    var progress: Int,
    var isReadingPages: List<Boolean>,
) : Parcelable {

    companion object {
        fun unknown() = BookThatRead(
            author = String(),
            createdAt = Date(),
            updatedAt = Date(),
            bookId = String(),
            objectId = String(),
            publicYear = String(),
            page = 0,
            title = String(),
            bookPdfUrl = String(),
            progress = 0,
            chapterCount = 0,
            chaptersRead = 0,
            poster = BookThatReadPoster(String(), String()),
            isReadingPages = emptyList()
        )
    }

    fun getCreatedAt(): String {
        val prettyTime = PrettyTime(Locale("ru"))
        return prettyTime.format(createdAt)
    }
}

@Parcelize
data class BookThatReadPoster(
    var name: String,
    var url: String,
) : Parcelable
